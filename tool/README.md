# initialising local GIT repo with contracts

This is a Golang script to easily init the repo. Go to your $OS directory and run it.

You could do the same by using commands like these

```
    git init --bare contracts-repo.git
    git clone contracts-repo.git/ contracts
    cd contracts
    cp -R ../META-INF .
    git add META-INF
    git commit -m "adding some contracts"
    git push origin master

```

the URI repository looking like file:// will used in the workshop.
