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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;

/**
 * Since ATs don't work properly on inner classes in CI/obf environments we simply copy the implementation
 * 1:1 copy of net.minecraft.client.renderer.block.model.ItemCameraTransforms.Deserializer
 */
@SideOnly(Side.CLIENT)
public class ItemCameraTransformsDeserializer implements JsonDeserializer<ItemCameraTransforms> {

    public static final ItemCameraTransformsDeserializer INSTANCE = new ItemCameraTransformsDeserializer();

    @Override
    public ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            throws
            JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        ItemTransformVec3f itemtransformvec3f = this.func_181683_a(p_deserialize_3_, jsonobject, "thirdperson_righthand");
        ItemTransformVec3f itemtransformvec3f1 = this.func_181683_a(p_deserialize_3_, jsonobject, "thirdperson_lefthand");

        if(itemtransformvec3f1 == ItemTransformVec3f.DEFAULT) {
            itemtransformvec3f1 = itemtransformvec3f;
        }

        ItemTransformVec3f itemtransformvec3f2 = this.func_181683_a(p_deserialize_3_, jsonobject, "firstperson_righthand");
        ItemTransformVec3f itemtransformvec3f3 = this.func_181683_a(p_deserialize_3_, jsonobject, "firstperson_lefthand");

        if(itemtransformvec3f3 == ItemTransformVec3f.DEFAULT) {
            itemtransformvec3f3 = itemtransformvec3f2;
        }

        ItemTransformVec3f itemtransformvec3f4 = this.func_181683_a(p_deserialize_3_, jsonobject, "head");
        ItemTransformVec3f itemtransformvec3f5 = this.func_181683_a(p_deserialize_3_, jsonobject, "gui");
        ItemTransformVec3f itemtransformvec3f6 = this.func_181683_a(p_deserialize_3_, jsonobject, "ground");
        ItemTransformVec3f itemtransformvec3f7 = this.func_181683_a(p_deserialize_3_, jsonobject, "fixed");
        return new ItemCameraTransforms(itemtransformvec3f1, itemtransformvec3f, itemtransformvec3f3, itemtransformvec3f2, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7);
    }

    private ItemTransformVec3f func_181683_a(JsonDeserializationContext p_181683_1_, JsonObject p_181683_2_, String p_181683_3_) {
        return p_181683_2_.has(p_181683_3_) ? (ItemTransformVec3f) p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
    }
}
