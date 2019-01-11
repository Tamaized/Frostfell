package tamaized.frostfell.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class WorldInfoFrostfell extends WorldInfo {

	private final WorldInfo delegate;

	private long worldTime;
	private int thunderTime;
	private boolean thundering;
	private int rainTime;
	private boolean raining;

	WorldInfoFrostfell(WorldInfo worldInfoIn) {
		this.delegate = worldInfoIn;
	}

	@Override
	public NBTTagCompound cloneNBTCompound(@Nullable NBTTagCompound nbt) {
		return this.delegate.cloneNBTCompound(nbt);
	}

	@Override
	public long getSeed() {
		return this.delegate.getSeed();
	}

	@Override
	public int getSpawnX() {
		return this.delegate.getSpawnX();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnX(int x) {
	}

	@Override
	public int getSpawnY() {
		return this.delegate.getSpawnY();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnY(int y) {
	}

	@Override
	public int getSpawnZ() {
		return this.delegate.getSpawnZ();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnZ(int z) {
	}

	@Override
	public long getWorldTotalTime() {
		return this.delegate.getWorldTotalTime();
	}

	@Override
	public void setWorldTotalTime(long time) {
	}

	@Override
	public long getWorldTime() {
		return worldTime;
	}

	@Override
	public void setWorldTime(long time) {
		worldTime = time;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public long getSizeOnDisk() {
		return this.delegate.getSizeOnDisk();
	}

	@Override
	public NBTTagCompound getPlayerNBTTagCompound() {
		return this.delegate.getPlayerNBTTagCompound();
	}

	@Override
	public String getWorldName() {
		return this.delegate.getWorldName();
	}

	@Override
	public void setWorldName(String worldName) {
	}

	@Override
	public int getSaveVersion() {
		return this.delegate.getSaveVersion();
	}

	@Override
	public void setSaveVersion(int version) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public long getLastTimePlayed() {
		return this.delegate.getLastTimePlayed();
	}

	@Override
	public boolean isThundering() {
		return thundering;
	}

	@Override
	public void setThundering(boolean thunderingIn) {
		thundering = thunderingIn;
	}

	@Override
	public int getThunderTime() {
		return thunderTime;
	}

	@Override
	public void setThunderTime(int time) {
		thunderTime = time;
	}

	@Override
	public boolean isRaining() {
		return raining;
	}

	@Override
	public void setRaining(boolean isRaining) {
		raining = isRaining;
	}

	@Override
	public int getRainTime() {
		return rainTime;
	}

	@Override
	public void setRainTime(int time) {
		rainTime = time;
	}

	@Override
	public GameType getGameType() {
		return this.delegate.getGameType();
	}

	@Override
	public void setSpawn(BlockPos spawnPoint) {
	}

	@Override
	public boolean isMapFeaturesEnabled() {
		return this.delegate.isMapFeaturesEnabled();
	}

	@Override
	public boolean isHardcoreModeEnabled() {
		return this.delegate.isHardcoreModeEnabled();
	}

	@Override
	public WorldType getTerrainType() {
		return this.delegate.getTerrainType();
	}

	@Override
	public void setTerrainType(WorldType type) {
	}

	@Override
	public boolean areCommandsAllowed() {
		return this.delegate.areCommandsAllowed();
	}

	@Override
	public void setAllowCommands(boolean allow) {
	}

	@Override
	public boolean isInitialized() {
		return this.delegate.isInitialized();
	}

	@Override
	public void setServerInitialized(boolean initializedIn) {
	}

	@Override
	public GameRules getGameRulesInstance() {
		return this.delegate.getGameRulesInstance();
	}

	@Override
	public EnumDifficulty getDifficulty() {
		return this.delegate.getDifficulty();
	}

	@Override
	public void setDifficulty(EnumDifficulty newDifficulty) {
	}

	@Override
	public boolean isDifficultyLocked() {
		return this.delegate.isDifficultyLocked();
	}

	@Override
	public void setDifficultyLocked(boolean locked) {
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void setDimensionData(DimensionType dimensionIn, NBTTagCompound compound) {
		this.delegate.setDimensionData(dimensionIn, compound);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public NBTTagCompound getDimensionData(DimensionType dimensionIn) {
		return this.delegate.getDimensionData(dimensionIn);
	}

	@Override
	public void setDimensionData(int dimensionID, NBTTagCompound compound) {
		this.delegate.setDimensionData(dimensionID, compound);
	}

	@Override
	public NBTTagCompound getDimensionData(int dimensionID) {
		return this.delegate.getDimensionData(dimensionID);
	}

}
