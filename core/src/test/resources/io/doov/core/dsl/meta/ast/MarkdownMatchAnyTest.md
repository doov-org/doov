* rule
   * when
      * match any
         * always true A
         * always false B
         * always false C
   * validate
* rule
   * when
      * match any
         * always true A
         * always true B
         * always true C
   * validate
* rule
   * when
      * match any
         * always false A
         * always true B
         * always true C and
         * always true D
   * validate
* rule
   * when
      * match any
         * always false A
         * always false B
         * always false C
   * validate
* rule
   * when
      * match any
         * always true A or
         * always false D
         * always false B
         * always false C
   * validate
* rule
   * when
      * match any
         * always false A
         * always true B
         * always true C
   * validate
* rule
   * when
      * match any
         * always false A
         * always false B
         * always true C and
         * always false D
   * validate
* rule
   * when
      * match any
         * zero < 4
         * yesterday before today
         * string field matches '^some.*'
   * validate
