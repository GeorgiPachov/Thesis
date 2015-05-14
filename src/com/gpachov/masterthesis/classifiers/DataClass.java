package com.gpachov.masterthesis.classifiers;

public enum DataClass {
	WORST {

		@Override
		public float upperLimit() {
			return 0.2f;
		}

		@Override
		public float lowerLimit() {
			return 0.0f;
		}
	},
	BAD {

		@Override
		public float upperLimit() {
			return 0.4f;
		}

		@Override
		public float lowerLimit() {
			return 0.2f;
		}
	},
	NEUTRAL {

		@Override
		public float upperLimit() {
			return 0.6f;
		}

		@Override
		public float lowerLimit() {
			return 0.4f;
		}
	},
	GOOD {
		@Override
		public float upperLimit() {
			return 0.8f;
		}

		@Override
		public float lowerLimit() {
			return 0.6f;
		}
	},
	GREAT {

		@Override
		public float upperLimit() {
			return 1.0f;
		}

		@Override
		public float lowerLimit() {
			return 0.8f;
		}
	};
	
	public float mean(){
		return (upperLimit() + lowerLimit()) / 2;
	}
	public abstract float upperLimit();
	public abstract float lowerLimit();
	
	public static class Utils{
		public static boolean isConsideredPositive(DataClass result){
			return GOOD.equals(result) || GREAT.equals(result);
 		}
		
		public static boolean isConsideredNegative(DataClass result) {
			return WORST.equals(result) || BAD.equals(result);
		}
		
		public static boolean isConsideredNeutral(DataClass result){
			return NEUTRAL.equals(result);
		}
	}
}
