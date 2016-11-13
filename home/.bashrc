#
# This file is executed by bash(1) for interactive non-login shells.
# It is used for interactive bash-specific shell initialization.
#

# If not running interactively, don't do anything
[[ $- != *i* ]] && return

# 256 color terminal
export TERM=xterm-256color

# Do not write lines that begin with a space to the history list.
# Do not write duplicate lines to the history list.
# See bash(1) for info.
HISTCONTROL=ignoreboth

# Set shell options. For a full list of options see bash(1).
# Append the history list to the file named by the value of the HISTFILE
# variable when the shell exits, rather than overwriting the file.
shopt -s histappend
# Do not allow the source (.) builtin to use the value of PATH to find the
# directory containing the file supplied as an argument.
shopt -u sourcepath
# Extended globbing pattern matching.
shopt -s extglob
# Null string expansion of globbing patterns that have no match.
# Note: turned off because it breaks tab completion.
#shopt -s nullglob
# Recursive globbing with `**`.
shopt -s globstar

# Enable the lesspipe filter for less for viewing a wider variety of files.
# See lesspipe(1) for more info.
[[ -x /usr/bin/lesspipe.sh ]] && eval "$(SHELL=/bin/sh lesspipe.sh)"

# Set the prompt
BLUE='\[\033[00;34m\]'
GREEN='\[\033[00;32m\]'
RESET='\[\033[00m\]'
PS1="${GREEN}\u@\h${RESET}:${BLUE}\w${GREEN}\$${RESET} "
PROMPT_DIRTRIM=3
unset BLUE GREEN RESET

# Set the colors used by ls. See dircolors(1) for options and fine-tuning.
[[ -f "${HOME}/.dircolors" ]] && DIRCOLORS="${HOME}/.dircolors" \
  && eval "$(dircolors --sh $DIRCOLORS)"

# Source alias definitions.
[[ -f "${HOME}/.bash_aliases" ]] && . "${HOME}/.bash_aliases"

# Enable bash completion.
if ! shopt -oq posix; then
  [[ -f /etc/bash_completion ]] && . /etc/bash_completion
  [[ -f "$HOME/.bash_completion" ]] && . "$HOME/.bash_completion"
fi

# Set default editor
export EDITOR=/usr/bin/vim

