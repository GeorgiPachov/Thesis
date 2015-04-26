package com.gpachov.masterthesis.classifiers;

public class ClassifierResult {
	
	enum Class { 
	CLASS_1 {
		@Override
		public float equivalence() {
			return 0.1f;
		}
	},
	CLASS_2 {
		@Override
		public float equivalence() {
			return 0.3f;
		}
	},
	CLASS_3 {
		@Override
		public float equivalence() {
			return 0.5f;
		}
	},
	CLASS_4 {
		@Override
		public float equivalence() {
			return 0.7f;
		}
	},
	CLASS_5 {
		@Override
		public float equivalence() {
			return 0.9f;
		}
	};
	public abstract float equivalence();
	}
	
	
	public boolean isConsideredPositive(ClassifierResult result) {
		return CLASS_4.equals(result) || CLASS_5.equals(result);
	}

	public boolean isConsideredNegative(ClassifierResult result) {
		return CLASS_1.equals(result) || CLASS_2.equals(result);
	}

	public boolean isConsideredNeutral(ClassifierResult result) {
		return CLASS_3.equals(result);
	}
}
