package org.vsw.nlp.query.eval;

import java.io.IOException;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;

public class If implements EvalExpression {
  private EvalExpression leftExp ;
  private EvalExpression rightExp ;
  private OpComparator   opComparator ;

  private EvalExpression matchExp ;
  private EvalExpression notMatchExp ;

  public String getName() { return "if"; }

  public If(String exp) throws Exception {
  	init(exp) ;
  }
  
  public void init(String exp) throws Exception {
    int idx = exp.indexOf('?') ;
    parseCondition(exp.substring(0, idx)) ;
    parseOperation(exp.substring(idx + 1)) ;
  }

  public Object eval(QueryContext context, QueryData doc) throws Exception {
    boolean compareResult = false ;
    if(opComparator instanceof NoOpComparator) compareResult = (Boolean) leftExp.eval(context, doc) ;
    else compareResult = opComparator.compare(leftExp, rightExp, context, doc) ;
    
    if(compareResult) {
      matchExp.eval(context, doc) ;
    } else {
      if(notMatchExp != null) notMatchExp.eval(context, doc) ;
    }
    return compareResult;
  }

  private void parseCondition(String exp) throws Exception {
    String[] pairexp ;
    if(exp.indexOf(" == ") > 0) {
      opComparator = new EqualOpComparator() ;
      pairexp = exp.split(" == ", 2) ;
    } else if(exp.indexOf(" != ") > 0) {
      opComparator = new NotEqualOpComparator() ;
      pairexp = exp.split(" != ", 2) ;
    } else if(exp.indexOf(" > ") > 0) {
      opComparator = new GreaterOpComparator() ;
      pairexp = exp.split(" > ", 2) ;
    } else if(exp.indexOf(" >= ") > 0) {
      opComparator = new GreaterOrEqualOpComparator() ;
      pairexp = exp.split(" >= ", 2) ;
    } else if(exp.indexOf(" < ") > 0) {
      opComparator = new LessOpComparator() ;
      pairexp = exp.split(" < ", 2) ;
    } else if(exp.indexOf(" <= ") > 0) {
      opComparator = new LessOrEqualOpComparator() ;
      pairexp = exp.split(" <= ", 2) ;
    } else {
      opComparator = new NoOpComparator() ;
      pairexp = new String[] { exp } ;
    }
    leftExp = EvalExpressionParser.parse(pairexp[0]) ;
    if(pairexp.length > 1) rightExp = EvalExpressionParser.parse(pairexp[1]) ;
  }

  private void parseOperation(String exp) throws Exception {
    String[] pairexp = exp.split(" : ", 2) ;
    this.matchExp = EvalExpressionParser.parse(pairexp[0]) ;
    if(pairexp.length == 2) this.notMatchExp = EvalExpressionParser.parse(pairexp[1]) ;
    if(matchExp == null) throw new RuntimeException("Evaluation expression is incorrect: " + exp) ;
  }

  static interface OpComparator {
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp, 
        QueryContext context, QueryData doc) throws Exception ;
  }

  static class NoOpComparator implements OpComparator{
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp, 
        QueryContext context, QueryData doc) throws Exception {
      return (Boolean) leftOp.eval(context, doc) ;
    }
  }

  static class EqualOpComparator implements OpComparator{
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp, 
        QueryContext context, QueryData doc) throws Exception {
      Comparable left = (Comparable)leftOp.eval(context, doc) ;
      Comparable right = (Comparable)rightOp.eval(context, doc) ;
      return (Boolean) (left.compareTo(right) == 0) ;
    }
  }

  static class NotEqualOpComparator implements OpComparator{
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp, 
        QueryContext context, QueryData doc) throws Exception {
      Comparable left = (Comparable)leftOp.eval(context, doc) ;
      Comparable right = (Comparable)rightOp.eval(context, doc) ;
      return (Boolean) (left.compareTo(right) != 0) ;
    }
  }

  static class LessOpComparator implements OpComparator{
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp, 
        QueryContext context, QueryData doc) throws Exception {
      Comparable left = (Comparable)leftOp.eval(context, doc) ;
      Comparable right = (Comparable)rightOp.eval(context, doc) ;
      return (Boolean) (left.compareTo(right) < 0) ;
    }
  }

  static class GreaterOpComparator implements OpComparator{
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp, 
        QueryContext context, QueryData doc) throws Exception {
      Comparable left = (Comparable)leftOp.eval(context, doc) ;
      Comparable right = (Comparable)rightOp.eval(context, doc) ;
      return (Boolean) (left.compareTo(right) > 0) ;
    }
  }

  static class GreaterOrEqualOpComparator implements OpComparator {
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp,
        QueryContext context, QueryData doc) throws Exception {
      Comparable left = (Comparable)leftOp.eval(context, doc) ;
      Comparable right = (Comparable)rightOp.eval(context, doc) ; 
      return (Boolean) (left.compareTo(right) >= 0) ;
    }
  }

  static class LessOrEqualOpComparator implements OpComparator {
    public boolean compare(EvalExpression leftOp, EvalExpression rightOp,
        QueryContext context, QueryData doc) throws Exception {
      Comparable left = (Comparable)leftOp.eval(context, doc) ;
      Comparable right = (Comparable)rightOp.eval(context, doc) ; 
      return (Boolean) (left.compareTo(right) <= 0) ;
    }
  }
}