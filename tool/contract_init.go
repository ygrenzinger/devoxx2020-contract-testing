package main

import (
	"errors"
	"fmt"
	"io/ioutil"
	"net/url"
	"os"
	"path/filepath"
	"time"

	"github.com/otiai10/copy"
	"gopkg.in/src-d/go-git.v4"
	"gopkg.in/src-d/go-git.v4/config"
	"gopkg.in/src-d/go-git.v4/plumbing/object"
)

// CheckIfError because Golang
func CheckIfError(step string, err error) {
	if err == nil {
		return
	}

	fmt.Printf("\x1b[31;1m%s\x1b[0m\n", fmt.Sprintf("error on step %s : %s", step, err))
	os.Exit(1)
}

// checkDirectory returns whether directory exists
func checkDirectory(path string) error {
	fileInfo, err := os.Stat(path)
	if err != nil {
		return err
	}
	if fileInfo.IsDir() {
		return nil
	}
	return errors.New(fileInfo.Name() + " is not a directory")
}

// retrieveReposPath retrieve dir from args or create one in temp
func retrieveReposPath() string {
	if len(os.Args) > 2 {
		err := checkDirectory(os.Args[2])
		CheckIfError("bad argument", err)
		return filepath.ToSlash(os.Args[2])
	}

	dirPath, err := ioutil.TempDir("", "devoxx")
	CheckIfError("creating temp dir", err)
	return filepath.ToSlash(dirPath)
}

// retrieveCurrentContractsPath retrieves the directory where are the current contracts
func retrieveCurrentContractsPath() string {
	if len(os.Args) > 1 {
		return filepath.ToSlash(os.Args[1])
	}
	return "../../META-INF"
}

func main() {
	contractsPath := retrieveCurrentContractsPath()
	err := checkDirectory(contractsPath)
	CheckIfError("contract path "+contractsPath, err)

	reposPath := retrieveReposPath()
	err = checkDirectory(reposPath)
	CheckIfError("repo path "+contractsPath, err)

	repo := reposPath + "/contracts"
	remote := reposPath + "/contracts.git"

	_, err = git.PlainInit(remote, true)
	CheckIfError("init remote", err)

	remoteURL, _ := url.Parse(remote)
	remoteURL.Scheme = "file"

	r, err := git.PlainInit(repo, false)
	CheckIfError("init repo", err)

	_, err = r.CreateRemote(&config.RemoteConfig{
		Name: "origin",
		URLs: []string{remoteURL.String()},
	})
	CheckIfError("creating remote "+remoteURL.String(), err)

	w, err := r.Worktree()
	CheckIfError("getting worktree", err)

	err = copy.Copy(contractsPath, repo+"/META-INF")
	CheckIfError("copying contracts", err)

	_, err = w.Add("META-INF")
	CheckIfError("adding to staging", err)

	_, err = w.Commit("commit original contracts", &git.CommitOptions{
		Author: &object.Signature{
			Name:  "contract testing workshop",
			Email: "contract-testing@devoxx.fr",
			When:  time.Now(),
		}})
	CheckIfError("commiting contracts", err)

	err = r.Push(&git.PushOptions{
		RemoteName: "origin",
	})
	CheckIfError("pushing to remote", err)

	fmt.Println("You can now use the following url for your contract repo: git://" + remoteURL.String())
}
