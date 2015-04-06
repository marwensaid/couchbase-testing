/*
 * Copyright Christophe Jeunesse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.dreameddeath.core.exception.dao;

/**
 * Created by ceaj8230 on 02/09/2014.
 */
public class DaoException extends Exception {
    public DaoException(Throwable e){
        super(e);
    }
    public DaoException(String message, Throwable e){
        super(message,e);
    }
    public DaoException(String message){
        super(message);
    }
}
