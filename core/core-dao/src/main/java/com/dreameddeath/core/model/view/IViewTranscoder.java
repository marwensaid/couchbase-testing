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

package com.dreameddeath.core.model.view;

import com.dreameddeath.core.exception.view.ViewDecodingException;
import com.dreameddeath.core.exception.view.ViewEncodingException;
import com.dreameddeath.core.model.view.impl.*;

/**
 * Created by ceaj8230 on 22/12/2014.
 */
public interface IViewTranscoder<T> {
    public Object encode(T key) throws ViewEncodingException;
    public T decode(Object value) throws ViewDecodingException;
    
    public static class Utils{
        public static IViewTranscoder<String> stringTranscoder(){return new ViewStringTranscoder();}
        public static IViewTranscoder<Double> doubleTranscoder(){return new ViewDoubleTranscoder();}
        public static IViewTranscoder<Long> longTranscoder(){return new ViewLongTranscoder();}
        public static IViewTranscoder<Boolean> booleanTranscoder(){return new ViewBooleanTranscoder();}

        public static IViewKeyTranscoder<String> stringKeyTranscoder(){return new ViewStringKeyTranscoder();}
        public static IViewKeyTranscoder<Double> doubleKeyTranscoder(){return new ViewDoubleKeyTranscoder();}
        public static IViewKeyTranscoder<Long> longKeyTranscoder(){return new ViewLongKeyTranscoder();}
        public static IViewKeyTranscoder<Boolean> booleanKeyTranscoder(){return new ViewBooleanKeyTranscoder();}

    }
}
