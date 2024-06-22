package com.example.sb.camel.projog;

import org.projog.core.predicate.AbstractPredicateFactory;
import org.projog.core.predicate.Predicate;
import org.projog.core.term.Atom;
import org.projog.core.term.Term;
import org.projog.core.term.TermUtils;

public class RetryablePredicateFactory extends AbstractPredicateFactory {

	@Override
	public Predicate getPredicate(Term arg1, Term arg2) {
		String csv = TermUtils.getAtomName(arg1);
		String[] split = csv.split(",");
		return new RetryablePredicate(split, arg2);
	}

	private static class RetryablePredicate implements Predicate {
		private final String[] split;
		private final Term target;
		private int idx;

		RetryablePredicate(String[] split, Term target) {
			this.split = split;
			this.target = target;
		}

		@Override
		public boolean evaluate() {
			while (idx < split.length) {
				target.backtrack();
				String next = split[idx++];
				if (target.unify(new Atom(next))) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean couldReevaluationSucceed() {
			return idx < split.length;
		}
	}
}