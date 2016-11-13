#
# Bash alias definitions.
#

# ls
alias ls='ls --color=auto'
alias ll='ls -l'
alias la='ls -a'
alias l1='ls -1'
alias la1='ls -1a'
alias lla='ls -la'
alias lsd='ls -d'

# grep
alias grep='grep --color=auto'
alias fgrep='fgrep --color=auto'
alias egrep='egrep --color=auto'

# dmesg
alias dmesg='dmesg --color=auto'

# sudo (hack to allow sudo with aliases)
alias sudo='sudo '

# apt-get
alias agi='apt-get install --no-install-recommends'
alias agu='apt-get upgrade -V'
alias agv='apt-get update'
alias agl='apt-get changelog'
alias acs='apt-cache search'
alias act='apt-cache show'
alias acp='apt-cache policy'

# sqlite3
alias sqlite='sqlite3'

