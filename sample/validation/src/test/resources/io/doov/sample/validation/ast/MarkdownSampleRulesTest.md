* rule
   * when
      * user id is not null
   * validate
* rule
   * when
      * user birthdate age at account creation date >= 18
   * validate
* rule
   * when
      * user birthdate age at account creation date with first day of year >= 18
   * validate
* rule
   * when
      * not
         * account company = BLABLACAR
   * validate
* rule
   * when
      * match all
         * user birthdate age at today >= 18
         * account email length is <= configuration max email size
         * account country = FR and
         * account phone number starts with '+33'
   * validate
* rule
   * when
      * user birthdate age at today >= 18 and
      * account email length is <= configuration max email size and
      * account country = FR and
      * account phone number starts with '+33'
   * validate
* rule
   * when
      * match all
         * user first name as a number -function-  = 1
   * validate
* rule
   * when
      * user last name is not null and
      * user last name matches '[A-Z]+' and
      * count
* account phone number is not null
* account email is not null >
      * 0
   * validate
* rule
   * when
      * favorite site name 1 match any -function- -function-
   * validate
* rule
   * when
      * count
         * user first name is not null
         * user last name is not null and
         * user last name matches '[A-Z]+' >=
      * 0
   * validate
* rule
   * when
      * user birthdate after user birthdate minus 1 day(s)
   * validate
* rule
   * when
      * account email matches '\w+[@]\w+.com' or
      * account email matches '\w+[@]\w+.fr'
   * validate
* rule
   * when
      * user birthdate age at today >= 18
   * validate
* rule
   * when
      * min
         * configuration min age
         * configuration max email size >=
      * 0
   * validate
* rule
   * when
      * sum
         * configuration min age x 0
         * configuration max email size x 1 >=
      * 0
   * validate
* rule
   * when
      * user birthdate as a number -function-  = 1980
   * validate
* rule
   * when
      * account timezone as a string -function-  contains '00:00'
   * validate
