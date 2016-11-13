#
# This file is executed by the command interpreter for login shells. It is not
# read by bash(1) if ~/.bash_profile or ~/.bash_login exist.
#

# ~/.profile is traditionally used for setting up the environment, including
# environment variables such as PATH, regardless of shell.


# Setup PATH, with custom binary locations taking precedence over system ones.
# $HOME/bin:/usr/local/{bin,sbin}:/usr/{bin,sbin}:/{bin,sbin}

# NOTE: /usr/local is considered obsolete by the Filesystem Hierarchy Standard.
# Package managers are used to install all packages under /usr, however, if any
# packages are installed by hand, they should go under /usr/local and the
# following locations added to the PATH.
#
# Host-specific command binaries
PATH="/usr/local/bin"
# Host-specific system binaries
PATH="${PATH}:/usr/local/sbin"

# Non-essential command binaries
PATH="${PATH}:/usr/bin"
# Non-essential system binaries
PATH="${PATH}:/usr/sbin"

# Essential command binaries
PATH="${PATH}:/bin"
# Essential system binaries
PATH="${PATH}:/sbin"

# Prepend bin directory installedy by pip if it exists
[[ -d "${HOME}/.local/bin" ]] && PATH="${HOME}/.local/bin:${PATH}"
# Prepend personal bin directory to PATH if it exists
[[ -d "${HOME}/bin" ]] && PATH="${HOME}/bin:${PATH}"

# Start ssh-agent, keep keys for 24 hours.
if [[ -z "${SSH_AGENT_PID:=$(pidof ssh-agent)}" ]]; then
  eval $(/usr/bin/ssh-agent -s -t 86400)
else
  export SSH_AUTH_SOCK=$(ls -t /tmp/ssh-*/agent.$((SSH_AGENT_PID - 1)) 2>/dev/null | head -1)
  export SSH_AGENT_PID
fi

# Automatically start x when logging in on tty6
[[ -z "$DISPLAY" && "$(/usr/bin/tty)" == /dev/tty6 ]] && /usr/bin/startx && logout

