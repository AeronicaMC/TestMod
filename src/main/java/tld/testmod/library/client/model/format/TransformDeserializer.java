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

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.TRSRTransformation;

import java.lang.reflect.Type;

public class TransformDeserializer implements JsonDeserializer<ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation>> {

    public static final TransformDeserializer INSTANCE = new TransformDeserializer();
    public static final Type TYPE = new TypeToken<ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation>>() {}.getType();

    public static String tag;

    @Override
    public ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement texElem = obj.get(tag);

        if(texElem != null && texElem.isJsonObject()) {
            ItemCameraTransforms itemCameraTransforms = context.deserialize(texElem.getAsJsonObject(), ItemCameraTransforms.class);
            return PerspectiveMapWrapper.getTransforms(itemCameraTransforms);
        }

        return ImmutableMap.of();
    }
}
