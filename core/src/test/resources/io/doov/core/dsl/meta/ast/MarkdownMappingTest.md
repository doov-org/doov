* map stringField and stringField2
   * using 'combine names'
   * to stringField2
* map '2000-01-01'
   * to dateField
* mappings
   * map '18'
      * to intField
   * map 'true'
      * to booleanField
   * map '2000-01-01'
      * to dateField
* mappings
   * mappings
      * when
         * {$String|stringField} = 'bing'
      * then
         * map 'www.bingue.com'
            * to {$String|stringField2}
   * mappings
      * when
         * {$String|stringField} = 'Google'
      * then
         * map 'www.gougeule.com'
            * to {$String|stringField2}
   * mappings
      * when
         * {$String|stringField} = 'Yahoo'
      * then
         * map 'www.yahou.com'
            * to {$String|stringField2}
* map '2000-01-01'
   * using 'date to string'
   * to stringField
* map 'true'
   * to booleanField
* mappings
   * when
      * dateField age at dateField2 >= '18'
   * then
      * map 'true'
         * to booleanField
   * else
      * map 'false'
         * to booleanField
* map '18'
   * to intField
* mappings
   * when
      * {$String|stringField} = 'Yahoo'
   * then
      * map 'www.yahou.com'
         * to {$String|stringField2}
