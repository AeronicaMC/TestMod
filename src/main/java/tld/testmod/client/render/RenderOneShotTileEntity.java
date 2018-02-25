package tld.testmod.client.render;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import tld.testmod.common.animation.OneShotTileEntity;

public class RenderOneShotTileEntity extends AnimationTESR<OneShotTileEntity>
{
    
    public static final RenderOneShotTileEntity INSTANCE = new RenderOneShotTileEntity();
    
    // public void renderTileEntityFast(OneShotTileEntity te, double x, double y, double z, float partialTick, int breakStage, VertexBuffer renderer)
    @Override
    public void renderTileEntityFast(OneShotTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if(!te.hasCapability(CapabilityAnimation.ANIMATION_CAPABILITY, null))
        {
            return;
        }
        if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos pos = te.getPos();
        IBlockAccess world = MinecraftForgeClient.getRegionRenderCache(te.getWorld(), pos);
        IBlockState state = world.getBlockState(pos);
        if(state.getPropertyKeys().contains(Properties.StaticProperty))
        {
            state = state.withProperty(Properties.StaticProperty, false);
        }
        if(state instanceof IExtendedBlockState)
        {
            IExtendedBlockState exState = (IExtendedBlockState)state;
            if(exState.getUnlistedNames().contains(Properties.AnimationProperty))
            {
                double time = Animation.getWorldTime(getWorld(), partialTicks);
                IAnimationStateMachine capability = te.getCapability(CapabilityAnimation.ANIMATION_CAPABILITY, null);
                if (capability != null)
                {
                    Pair<IModelState, Iterable<Event>> pair = capability.apply((float)time);
                    handleEvents(te, (float) time, pair.getRight());

                    IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(exState.getClean());
                    exState = exState.withProperty(Properties.AnimationProperty, pair.getLeft());

                    buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());

                    blockRenderer.getBlockModelRenderer().renderModel(world, model, exState, pos, buffer, false);
                }
            }
        }
    }
    @Override
    public void handleEvents(OneShotTileEntity te, float time, Iterable<Event> pastEvents)
    {
        super.handleEvents(te, time, pastEvents);
        te.handleEvents(time, pastEvents);
    }
    
}
