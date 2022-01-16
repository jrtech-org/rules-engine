/*
 * Copyright (c) 2016-2026 Jumin Rubin
 * LinkedIn: https://www.linkedin.com/in/juminrubin/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jrtech.engines.rules.model;

public interface INamedObject {

    public static final String SYSTEM_ORGANIZATION = "System";
    
    public static final String FIELD_OBJECT_ID = "objectId";
    
    public static final String FIELD_MODULE = "module";
    
    public static final String FIELD_NAME = "name";
    
    public static final String FIELD_OWNER = "owner";
    
    public static final String BASE_ALLOWED_CHARACTERS_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
    
    public static final String PHYSICAL_NAME_ALLOWED_CHARACTERS_STRING = BASE_ALLOWED_CHARACTERS_STRING;
    
}
