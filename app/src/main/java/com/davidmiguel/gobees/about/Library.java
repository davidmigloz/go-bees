/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.about;

/**
 * Models a dependence of the app.
 */
class Library {

    private String name;
    private License license;

    Library(String name, License license) {
        this.name = name;
        this.license = license;
    }

    public String getName() {
        return name;
    }

    public License getLicense() {
        return license;
    }

    public enum License {
        APACHE2("Apache v2.0"),
        GPL3("GPLv3"),
        MIT("MIT"),
        BSD("BSD"),
        CCBYSA4("CC BY-SA 4.0");

        private final String name;

        License(String s) {
            name = s;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
