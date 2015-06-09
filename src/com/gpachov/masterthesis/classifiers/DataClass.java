package com.gpachov.masterthesis.classifiers;

public enum DataClass {
	BAD {

		@Override
		public float upperLimit() {
			return 0.4f;
		}

		@Override
		public float lowerLimit() {
			return 0.0f;
		}
	},
	GOOD {
		@Override
		public float upperLimit() {
			return 0.6f;
		}

		@Override
		public float lowerLimit() {
			return 1.0f;
		}
	};
	public abstract float upperLimit();
	public abstract float lowerLimit();
	
	public static class Utils{
		public static boolean isConsideredPositive(DataClass result){
			return GOOD.equals(result);
 		}
	}
}
