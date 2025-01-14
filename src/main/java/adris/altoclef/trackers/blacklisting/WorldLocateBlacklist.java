package adris.altoclef.trackers.blacklisting;

import adris.altoclef.AltoClef;
import adris.altoclef.Debug;
import adris.altoclef.util.MiningRequirement;
import adris.altoclef.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

/**
 * Sometimes we will try to access something and fail TOO many times.
 *
 * This lets us know that a block is unreachable, and will ignore it from the search intelligently.
 */
public class WorldLocateBlacklist extends AbstractObjectBlacklist<BlockPos> {

    @Override
    protected Vec3d getPos(BlockPos item) {
        return WorldUtil.toVec3d(item);
    }
}
