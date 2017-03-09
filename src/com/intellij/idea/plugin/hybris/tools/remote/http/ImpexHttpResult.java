/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.tools.remote.http;

import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by alexander on 07.03.17.
 */
public class ImpexHttpResult {

    private boolean hasError;
    private String errorMessage;
    private String detailMessage;
    
    private String output;
    private int statusCode;


    private ImpexHttpResult() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutput() {
        return output;
    }

    static public class ImpexHttpResultBuilder {

        private boolean hasError = false;
        private String errorMessage;
        private String detailMessage;

        private String output;
        private int statusCode = SC_OK;

        private ImpexHttpResultBuilder() {
        }

        public static ImpexHttpResultBuilder createResult() {
            return new ImpexHttpResultBuilder();
        }

        public ImpexHttpResultBuilder errorMessage(final String errorMessage) {
            if (isNotEmpty(errorMessage)) {
                this.errorMessage = errorMessage;
                this.hasError = true;
            }
            return this;
        }

        public ImpexHttpResultBuilder detailMessage(final String detailMessage) {
            if (isNotEmpty(detailMessage)) {
                this.detailMessage = detailMessage;
                this.hasError = true;
            }
            return this;
        }

        public ImpexHttpResultBuilder output(final String output) {
            this.output = output;
            return this;
        }

        public ImpexHttpResultBuilder httpCode(final int statusCode) {
            this.statusCode = statusCode;
            return this;
        }


        public ImpexHttpResult build() {
            final ImpexHttpResult httpResult = new ImpexHttpResult();
            httpResult.hasError = this.hasError;
            httpResult.errorMessage = this.errorMessage;
            httpResult.detailMessage = this.detailMessage;
            httpResult.output = this.output;
            httpResult.statusCode =  this.statusCode;
            
            return httpResult;
        }

    }
}
