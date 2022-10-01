package funorb.shatteredplans.map.generator;

import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;

public interface MapGenerator {
  StarSystem DUMMY_SYSTEM = new StarSystem(-1, -1, -1, -1);

  Map generate() throws MapGenerationFailure;
}
