/**
 * Copyright {2016} Paul Boese aka Aeronica
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
package tld.testmod.common.entity.living;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class EntityTestMob extends EntityMob
{

    public EntityTestMob(World worldIn)
    {
        super(worldIn);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.Entity#addTrackingPlayer(net.minecraft.entity.player.EntityPlayerMP)
     */
    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub
        super.addTrackingPlayer(player);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.monster.EntityMob#attackEntityAsMob(net.minecraft.entity.Entity)
     */
    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        // TODO Auto-generated method stub
        return super.attackEntityAsMob(entityIn);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLiving#dropLoot(boolean, int, net.minecraft.util.DamageSource)
     */
    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        // TODO Auto-generated method stub
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLivingBase#fall(float, float)
     */
    @Override
    public void fall(float distance, float damageMultiplier)
    {
        // TODO Auto-generated method stub
        super.fall(distance, damageMultiplier);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLiving#getAmbientSound()
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        // TODO Auto-generated method stub
        return super.getAmbientSound();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.monster.EntityMob#getDeathSound()
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        // TODO Auto-generated method stub
        return super.getDeathSound();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.monster.EntityMob#getHurtSound()
     */
    @Override
    protected SoundEvent getHurtSound()
    {
        // TODO Auto-generated method stub
        return super.getHurtSound();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLiving#getMaxFallHeight()
     */
    @Override
    public int getMaxFallHeight()
    {
        // TODO Auto-generated method stub
        return super.getMaxFallHeight();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLiving#getMaxSpawnedInChunk()
     */
    @Override
    public int getMaxSpawnedInChunk()
    {
        // TODO Auto-generated method stub
        return super.getMaxSpawnedInChunk();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLiving#getNavigator()
     */
    @Override
    public PathNavigate getNavigator()
    {
        // TODO Auto-generated method stub
        return super.getNavigator();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLivingBase#getSoundPitch()
     */
    @Override
    protected float getSoundPitch()
    {
        // TODO Auto-generated method stub
        return super.getSoundPitch();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLivingBase#getSoundVolume()
     */
    @Override
    protected float getSoundVolume()
    {
        // TODO Auto-generated method stub
        return super.getSoundVolume();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLivingBase#isOnLadder()
     */
    @Override
    public boolean isOnLadder()
    {
        // TODO Auto-generated method stub
        return super.isOnLadder();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLivingBase#moveEntityWithHeading(float, float)
     */
    @Override
    public void moveEntityWithHeading(float strafe, float forward)
    {
        // TODO Auto-generated method stub
        super.moveEntityWithHeading(strafe, forward);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLivingBase#onDeathUpdate()
     */
    @Override
    protected void onDeathUpdate()
    {
        // TODO Auto-generated method stub
        super.onDeathUpdate();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.monster.EntityMob#onUpdate()
     */
    @Override
    public void onUpdate()
    {
        // TODO Auto-generated method stub
        super.onUpdate();
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLiving#readEntityFromNBT(net.minecraft.nbt.NBTTagCompound)
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        // TODO Auto-generated method stub
        super.readEntityFromNBT(compound);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.Entity#removeTrackingPlayer(net.minecraft.entity.player.EntityPlayerMP)
     */
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub
        super.removeTrackingPlayer(player);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLivingBase#updateFallState(double, boolean, net.minecraft.block.state.IBlockState, net.minecraft.util.math.BlockPos)
     */
    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
        // TODO Auto-generated method stub
        super.updateFallState(y, onGroundIn, state, pos);
    }

    /* (non-Javadoc)
     * @see net.minecraft.entity.EntityLiving#writeEntityToNBT(net.minecraft.nbt.NBTTagCompound)
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        // TODO Auto-generated method stub
        super.writeEntityToNBT(compound);
    }

}
