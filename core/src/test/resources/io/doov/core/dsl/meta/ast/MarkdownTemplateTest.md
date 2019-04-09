* map {$String|param1}
   * to {$String|param2}
* rule
   * when
      * {$String|param1} contains 'google'
   * validate
* rule
   * when
      * {$String|param1} contains {$String|param2} as a string -function-
   * validate
