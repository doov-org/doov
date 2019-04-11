* rule
   * when
      * match all
         * always true 'A'
         * always true 'B'
         * always false 'C'
   * validate
* rule
   * when
      * match all
         * always false 'A'
         * always false 'B'
         * always false 'C'
   * validate
* rule
   * when
      * match all
         * always true 'A'
         * always true 'B'
         * always true 'C'
   * validate
* rule
   * when
      * match all
         * always true 'A'
         * always false 'B'
         * always false 'C'
   * validate
* rule
   * when
      * match all
         * zero > '4'
         * yesterday after today
         * string field matches '^other.*'
   * validate
