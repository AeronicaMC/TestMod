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
package tld.testmod.library.client.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.Type;

/**
 * Since ATs don't work properly on inner classes in CI/obf environments we simply copy the implementation
 * 1:1 copy of net.minecraft.client.renderer.block.model.ItemTransformVec3f.Deserializer
 */
@SideOnly(Side.CLIENT)
public class ItemTransformVec3fDeserializer implements JsonDeserializer<ItemTransformVec3f> {

    public static final ItemTransformVec3fDeserializer INSTANCE = new ItemTransformVec3fDeserializer();

    private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

    @Override
    public ItemTransformVec3f deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        Vector3f vector3f = this.parseVector3f(jsonobject, "rotation", ROTATION_DEFAULT);
        Vector3f vector3f1 = this.parseVector3f(jsonobject, "translation", TRANSLATION_DEFAULT);
        vector3f1.scale(0.0625F);
        vector3f1.x = MathHelper.clamp(vector3f1.x, -5.0F, 5.0F);
        vector3f1.y = MathHelper.clamp(vector3f1.y, -5.0F, 5.0F);
        vector3f1.z = MathHelper.clamp(vector3f1.z, -5.0F, 5.0F);
        Vector3f vector3f2 = this.parseVector3f(jsonobject, "scale", SCALE_DEFAULT);
        vector3f2.x = MathHelper.clamp(vector3f2.x, -4.0F, 4.0F);
        vector3f2.y = MathHelper.clamp(vector3f2.y, -4.0F, 4.0F);
        vector3f2.z = MathHelper.clamp(vector3f2.z, -4.0F, 4.0F);
        return new ItemTransformVec3f(vector3f, vector3f1, vector3f2);
    }

    private Vector3f parseVector3f(JsonObject jsonObject, String key, Vector3f defaultValue) {
        if(!jsonObject.has(key)) {
            return defaultValue;
        }
        else {
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonObject, key);

            if(jsonarray.size() != 3) {
                throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
            }
            else {
                float[] afloat = new float[3];

                for(int i = 0; i < afloat.length; ++i) {
                    afloat[i] = JsonUtils.getFloat(jsonarray.get(i), key + "[" + i + "]");
                }

                return new Vector3f(afloat[0], afloat[1], afloat[2]);
            }
        }
    }
}
