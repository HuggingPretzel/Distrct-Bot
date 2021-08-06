/**
 *      Copyright 2021 Daniel Sanchez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.districtmeps.dbot.config;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

public class Config extends JSONObject{

    private static Config instance;

    public Config(File file) throws IOException{
        super(new ConfigLoader().load(file));

        instance = this;
    }

    public static Config getInstance(){
        return instance;
    }
    
}