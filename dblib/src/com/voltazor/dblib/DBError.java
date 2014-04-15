/*
 * Copyright (c) 2014 Dmitriy Dovbnya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.voltazor.dblib;

import android.text.TextUtils;

public class DBError extends Exception {

    private String message;

    public DBError(String message) {
        super(message);
        this.message = message;
    }

    public DBError(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String getMessage() {
        return TextUtils.isEmpty(message) ? "DBError: " + super.getMessage() : message;
    }

    public boolean isNoData() {
        return (getCause() instanceof NoDataException);
    }

}
