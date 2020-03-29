// The MIT License (MIT) Copyright (c) 2013-2014 Slime Knights (mDiyo,
// fuj1n, Sunstrike, progwml6, pillbox, alexbegt)
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
// CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
// TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
// SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// https://github.com/SlimeKnights/TinkersConstruct
//
package tld.testmod.library.client.model.format;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Deseralizes a json in the format of { "textures": { "foo": "texture",... }}
 * Ignores all invalid json
 */
public class ModelTextureDeserializer implements JsonDeserializer<Map<String, String>> {

    public static final ModelTextureDeserializer INSTANCE = new ModelTextureDeserializer();
    public static final Type TYPE = new TypeToken<Map<String, String>>() {}.getType();

    private static final Gson GSON = new Gson();

    @Override
    public Map<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        JsonElement texElem = obj.get("textures");

        if(texElem == null) {
            throw new JsonParseException("Missing textures entry in json");
        }

        return GSON.fromJson(texElem, TYPE);
    }
}
