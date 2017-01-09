"
" Vim initialization file. See :help vimrc for more info.
"

" FILES
" -----
set nocompatible " Vi-incompatible mode (vim); must be first setting in file.
set history=1000 " Number of lines of history stored in buffer.
filetype indent on " Load filetype-specific indentation files.
filetype plugin on " Enable filetype-specific plugins.
set encoding=utf8 " Set encoding.
set fileformats=unix,dos,mac " Order of filetypes to try to use.
set hidden " Change bufferes without writing to file.
set autoread " Re-read file when it is changed outside of vim.
set writebackup " Temp backup during write, removed on success.
set viminfo='20,<50,s10,h,% " Remember marks, registers, searches, buffer list.
" Restore last cursor position:
function! Restore_cursor()
  if line("'\"") > 0 && line("'\"") <= line("$")
    exe "normal! g'\""
  endif
endfunction
autocmd BufReadPost * call Restore_cursor()
" Remove trailing whitespace on save:
autocmd BufWritePre * %s/\s\+$//e


" COLORS
" ------
set background=dark " Use dark variant of colorscheme.
"colorscheme slate " Use slate scheme.
colorscheme solarized " Use solarized scheme.
syntax enable " Enable syntax highlighting.


" INDENTATION
" -----------
set expandtab " Use spaces instead of tabs when inserting.
autocmd FileType make setlocal noexpandtab " Use tabs instead of spaces for make files (required).
set tabstop=4 " Number of spaces to display a \t character.
set softtabstop=2 " Number of spaces to insert on indent.
set shiftwidth=2 " Number of spaces to shift (indent); if 0, tabstop value used as fallback.
set shiftround " Round shift (indent) to multiple of shiftwidth.
set autoindent " Auto-indent subsequent line to the level of the current line on CR.
set smarttab " Indent according to shiftwidth, instead of [soft]tabstop.
set nosmartindent " ???


" FOLDING
" -------
set foldenable " Enable folding.
set foldlevelstart=10 " Depth at which to start folding.
set foldnestmax=10 " Max depth for nested folds.
set foldmethod=indent " Fold based on indent level; other methods available.


" UI
" --
set number " Display line numbers.
" Highlight left line-number gutter slightly:
highlight LineNr ctermbg=233 guibg=#111111
set showcmd " Display current command in bottom gutter.
set cmdheight=2 " Command line height.
set cursorline " Highlight current line.
set ruler " Display cursor position in bottom gutter.
set wildmenu " Enable file auto-completion.
set wildmode=list:longest " File auto-completion settings.
set wildignore=*.o,*~,*.pyc " File auto-completion ignored files.
set scrolloff=5 " Minimum lines to show above/below cursor.
set lazyredraw " Do not redraw during macro execution for better performance.
set showmatch " Show matching brackets.
set noerrorbells " Silence bell.
set t_vb= " No visual error bell.
set timeoutlen=500 " Time (ms) for a key sequence to complete.
set shellcmdflag=-ic " Flags to pass to shell when running commands using `!`.
" Color 80th and 100th columns if possible:
if exists('+colorcolumn')
  set colorcolumn=81,101
  highlight ColorColumn ctermbg=233 guibg=#121212
endif


" SEARCH
" ------
set incsearch " Incremental search as characters are entered.
set hlsearch " Highlight search matches.
set noignorecase " Do not ignore case when searching.


" MOVEMENT & MAPPINGS
" -------------------
set backspace=indent,eol,start " Set backspace key behavior.
set whichwrap+=<,>,h,l " Movement keys allowed to wrap over eol.
" Move vertically by visual line, rather than true line:
nnoremap j gj
nnoremap k gk
nnoremap <down> gj
nnoremap <up> gk
" Split lines/add new lines:
nnoremap <c-j> i<cr><esc>
nnoremap <cr> o<esc>
" Some shortcuts:
let mapleader=","
nnoremap <leader>w :w<cr>
nnoremap <leader>q :q<cr>
nnoremap <leader>wq :wq<cr>

