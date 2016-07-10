#### Utils

* `ImageInterceptActivity` -- Workaround for bug in Gmail. If there's not component that can handle
  a "view image" Intent, Gmail says "There is no app on your device that can show this file type".
  However if there _is_ a component that can handle that Intent, then Gmail displays the image
  without using it. This Activity does nothing, simply declares that it _can_ view images.
* `PasswordInputMethodService` -- Secure keyboard for entering passwords, since internet-enabled
  keyboards should not be trusted with sensitive input.
  Features a trustworthy green background so you can feel safe entering your password, with
  all printable ASCII symbols on the same interface; single taps to enter letters/numbers;
  long-presses to enter all other symbols.

