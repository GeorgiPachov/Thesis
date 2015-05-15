package com.gpachov.masterthesis.classifiers;

public enum DataClass {
	BAD {

		@Override
		public float upperLimit() {
			return 0.6f;
		}

		@Override
		public float lowerLimit() {
			return 0.2f;
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
	}, NEUTRAL {
	    @Override
	    public float upperLimit() {
		return 0.6f;
	    }

	    @Override
	    public float lowerLimit() {
		return 0.6f;
	    }
	};
	public float mean(){
		return (upperLimit() + lowerLimit()) / 2;
	}
	public abstract float upperLimit();
	public abstract float lowerLimit();
	
	public static class Utils{
		public static boolean isConsideredPositive(DataClass result){
			return GOOD.equals(result);
 		}
	}
}
