package tamaized.frostfell.world;

import com.google.common.collect.Lists;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import java.util.List;
import java.util.Random;

public class BiomeProviderModded extends BiomeProvider {

	private GenLayer genBiomes;
	/**
	 * A GenLayer containing the indices into Biome.biomeList[]
	 */
	private GenLayer biomeIndexLayer;
	/**
	 * The BiomeCache object for this world.
	 */
	private BiomeCache biomeCache;
	/**
	 * A list of biomes that the player can spawn in.
	 */
	private List<Biome> biomesToSpawnIn;

	private BiomeProviderModded(IGenLayer gen) {
		biomeCache = new BiomeCache(this);
		biomesToSpawnIn = Lists.newArrayList(gen.getAllowedBiomes());
	}

	private BiomeProviderModded(long seed, WorldType worldType, IGenLayer gen) {
		this(gen);
		GenLayer[] agenlayer = gen.initializeAllBiomeGenerators(seed, worldType);
		genBiomes = agenlayer[0];
		biomeIndexLayer = agenlayer[1];
	}

	public BiomeProviderModded(World world, IGenLayer gen) {
		this(world.getSeed(), world.getWorldInfo().getTerrainType(), gen);
	}

	/**
	 * Gets the list of valid biomes for the player to spawn in.
	 */
	@Override
	public List<Biome> getBiomesToSpawnIn() {
		return biomesToSpawnIn;
	}

	/**
	 * Returns the Biome related to the x, z position on the world.
	 */
	@Override
	public Biome getBiome(BlockPos pos, Biome defaultBiome) {
		return biomeCache.getBiome(pos.getX(), pos.getZ(), defaultBiome);
	}

	/**
	 * Returns an array of biomes for the location input.
	 */
	@Override
	public Biome[] getBiomesForGeneration(Biome[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		IntCache.resetIntCache();

		if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
			par1ArrayOfBiomeGenBase = new Biome[par4 * par5];
		}

		int[] aint = genBiomes.getInts(par2, par3, par4, par5);

		try {
			for (int i = 0; i < par4 * par5; ++i) {
				par1ArrayOfBiomeGenBase[i] = Biome.getBiome(aint[i]);
				if(par1ArrayOfBiomeGenBase[i] == null)
					par1ArrayOfBiomeGenBase[i] = biomesToSpawnIn.get(0);
			}

			return par1ArrayOfBiomeGenBase;
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
			crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(par1ArrayOfBiomeGenBase.length));
			crashreportcategory.addCrashSection("x", Integer.valueOf(par2));
			crashreportcategory.addCrashSection("z", Integer.valueOf(par3));
			crashreportcategory.addCrashSection("w", Integer.valueOf(par4));
			crashreportcategory.addCrashSection("h", Integer.valueOf(par5));
			throw new ReportedException(crashreport);
		}
	}

	/**
	 * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false, don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
	 */
	@Override
	public Biome[] getBiomes(Biome[] listToReuse, int x, int y, int width, int length, boolean cacheFlag) {
		IntCache.resetIntCache();

		if (listToReuse == null || listToReuse.length < width * length) {
			listToReuse = new Biome[width * length];
		}

		if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (y & 15) == 0) {
			Biome[] abiomegenbase1 = biomeCache.getCachedBiomes(x, y);
			System.arraycopy(abiomegenbase1, 0, listToReuse, 0, width * length);
			return listToReuse;
		} else {
			int[] aint = biomeIndexLayer.getInts(x, y, width, length);

			for (int i = 0; i < width * length; ++i) {
				listToReuse[i] = Biome.getBiome(aint[i]);
				if(listToReuse[i] == null)
					listToReuse[i] = biomesToSpawnIn.get(0);
			}
			return listToReuse;
		}
	}

	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean areBiomesViable(int x, int y, int z, List par4List) {
		IntCache.resetIntCache();
		int l = x - z >> 2;
		int i1 = y - z >> 2;
		int j1 = x + z >> 2;
		int k1 = y + z >> 2;
		int l1 = j1 - l + 1;
		int i2 = k1 - i1 + 1;
		int[] aint = genBiomes.getInts(l, i1, l1, i2);

		try {
			for (int j2 = 0; j2 < l1 * i2; ++j2) {
				Biome biomegenbase = Biome.getBiome(aint[j2]);

				if (!par4List.contains(biomegenbase)) {
					return false;
				}
			}

			return true;
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
			crashreportcategory.addCrashSection("Layer", genBiomes.toString());
			crashreportcategory.addCrashSection("x", Integer.valueOf(x));
			crashreportcategory.addCrashSection("z", Integer.valueOf(y));
			crashreportcategory.addCrashSection("radius", Integer.valueOf(z));
			crashreportcategory.addCrashSection("allowed", par4List);
			throw new ReportedException(crashreport);
		}
	}

	/**
	 * Finds a valid position within a range, that is in one of the listed biomes. Searches {par1,par2} +-par3 blocks. Strongly favors positive y positions.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public BlockPos findBiomePosition(int x, int y, int z, List par4List, Random random) {
		IntCache.resetIntCache();
		int l = x - z >> 2;
		int i1 = y - z >> 2;
		int j1 = x + z >> 2;
		int k1 = y + z >> 2;
		int l1 = j1 - l + 1;
		int i2 = k1 - i1 + 1;
		int[] aint = genBiomes.getInts(l, i1, l1, i2);
		BlockPos chunkposition = null;
		int j2 = 0;

		for (int k2 = 0; k2 < l1 * i2; ++k2) {
			int l2 = l + k2 % l1 << 2;
			int i3 = i1 + k2 / l1 << 2;
			Biome biomegenbase = Biome.getBiome(aint[k2]);

			if (par4List.contains(biomegenbase) && (chunkposition == null || random.nextInt(j2 + 1) == 0)) {
				chunkposition = new BlockPos(l2, 0, i3);
				++j2;
			}
		}

		return chunkposition;
	}

	/**
	 * Calls the WorldChunkManager's biomeCache.cleanupCache()
	 */
	@Override
	public void cleanupCache() {
		biomeCache.cleanupCache();
	}
}
