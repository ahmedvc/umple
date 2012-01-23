/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.15.0.963 modeling language!*/

package cruise.umple.compiler;
import cruise.umple.util.*;
import java.util.*;

public class GeneratorHelper
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GeneratorHelper()
  {}

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {}
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  public static void postpare(UmpleModel model)
  {
    postpareClass(model);
    postpareStateMachine(model);
    postpareTrace(model);
    
    for (UmpleClass aClass : model.getUmpleClasses())
    {
      postpare(aClass);
    }  
  }

  // Undo all class level internal changes
  private static void postpare(UmpleClass aClass)
  {
    postpareClass(aClass);
    postpareStateMachine(aClass);
    postpareTrace(aClass);
  }
public static void postpareClass(UmpleModel model)
  {
    int maxIndex = model.numberOfUmpleClasses() - 1;
    for (int i=maxIndex; i>=0; i--)
    {
      UmpleClass c = model.getUmpleClass(i);
      if (c.getIsInternal())
      {
        model.removeUmpleClass(c);
      }
    }
  }
  
  // Remove all internally added attributes / associations of a class
  private static void postpareClass(UmpleClass aClass)
  {
    int maxIndex = aClass.numberOfCodeInjections() - 1;
    for (int i=maxIndex; i>=0; i--)
    {
      CodeInjection ci = aClass.getCodeInjection(i);
      if (ci.getIsInternal())
      {
        aClass.removeCodeInjection(ci);
      }
    }
    
    maxIndex = aClass.numberOfDepends() - 1;
    for (int i=maxIndex; i>=0; i--)
    {
      Depend d = aClass.getDepend(i);
      if (d.getIsInternal())
      {
        aClass.removeDepend(d);
      }      
    }  
  }  
  
  public static String toCode(List<CodeInjection> allCodeInjections)
  {
    String asCode = null;
    if (allCodeInjections != null)
    {
      for (CodeInjection inject : allCodeInjections)
      {
        if (asCode == null)
        {
          asCode = inject.getCode();
        }
        else
        {
          asCode = StringFormatter.format("{0}\n    {1}",asCode,inject.getCode());
        }
      }
    }
    return asCode;
  }
private static void postpareStateMachine(UmpleModel aModel)
  {
    
  }

  // Remove all internal state machine entities
  private static void postpareStateMachine(UmpleClass aClass)
  {
    List<State> shouldDelete = new ArrayList<State>();
    for (StateMachine sm : aClass.getAllStateMachines())
    {
      postpareInternalStates(sm,shouldDelete);
    }

    // Remove all internally created actions
    for (StateMachine sm : aClass.getAllStateMachines())
    {
      for (State s : sm.getStates())
      {
        for (int i=s.numberOfTransitions()-1; i>=0; i--)
        {
          Transition t = s.getTransition(i);
          if (t.getIsInternal())
          {
            t.delete();
          }
        }

        for (int i=s.numberOfActions()-1; i>=0; i--)
        {
          Action a = s.getAction(i);
          if (a.getIsInternal())
          {
            s.removeAction(a);
          }
        }
      }
    }
    
    for (int i=shouldDelete.size()-1; i>=0; i--)
    {
      State s = shouldDelete.get(i);
      s.delete();
    }
  }  

  // Add the necessary entry action to delete the object once the final state is reached
  public static void prepareFinalState(StateMachine sm, Map<String,String> lookups)
  {
    for (State s : sm.getStates())
    {
      if (s.isFinalState())
      {
        Action entryAction = new Action(lookups.get("deleteActionCode"));
        entryAction.setIsInternal(true);
        entryAction.setActionType("entry");
        s.addAction(entryAction);
      }
    }
  }  

  // Add the necessary before / after hooks to support nested state machines
  public static void prepareNestedStateMachine(StateMachine sm, int concurrentIndex, Map<String,String> lookups)
  {
    String entryEventName = lookups.get("entryEventName");
    String exitEventName = lookups.get("exitEventName");
    String parentEntryActionCode = lookups.get("parentEntryActionCode");
    String parentExitActionCode = lookups.get("parentExitActionCode");
  
    State parentState = sm.getParentState();
    StateMachine firstSm = parentState.getNestedStateMachine(0);
    State nullState = sm.addState("Null",0);
    nullState.setIsInternal(true);

    if (sm.getStartState() != null)
    {
  
      if (concurrentIndex == 0 && parentExitActionCode != null)
      {
        Action parentExitAction = new Action(parentExitActionCode);
        parentExitAction.setIsInternal(true);
        parentExitAction.setActionType("exit");
        parentState.addAction(parentExitAction,0);
      }

      Event enterEvent = firstSm.findOrCreateEvent(entryEventName);
      enterEvent.setIsInternal(true);
      Transition enterTransition = new Transition(nullState,sm.getStartState());
      enterTransition.setIsInternal(true);
      enterTransition.setEvent(enterEvent);

      Event exitEvent = firstSm.findOrCreateEvent(exitEventName);
      exitEvent.setIsInternal(true);
  
      for (State state : sm.getStates())
      {
        if (state == nullState) { continue; }
        Transition exitTransition = state.addTransition(nullState,0);
        exitTransition.setIsInternal(true);
        exitTransition.setEvent(exitEvent);
      }

      Action parentEntryAction = new Action(parentEntryActionCode);
      parentEntryAction.setActionType("entry");
      parentEntryAction.setIsInternal(true);
      parentState.addAction(parentEntryAction); 
    }  
  }

  // Mark all internal states are ready for deletion
  private static void postpareInternalStates(StateMachine sm, List<State> shouldDelete)
  {
    for (int i=sm.numberOfStates() - 1; i >= 0; i--)
    {
      State s = sm.getState(i);
      if (s.getIsInternal())
      {
        shouldDelete.add(s);
      }       
    }
  }
private static void postpareTrace(UmpleModel aModel)
  {}

  // Currently no internal trace entities to remove at the class level
  private static void postpareTrace(UmpleClass aClass)
  {}
  
  public static void prepareTraceDirectiveAttributeInject( TraceDirective traceDirective, CodeTranslator t, Attribute_TraceItem traceAttr, Attribute attr, String attrCode, String conditionType) 
  {
	  if( traceAttr.getTraceSet() == true && traceAttr.getTraceGet() == false )
		  prepareTraceDirectiveInject(traceDirective,t,attr,attrCode,conditionType,"setMethod");
	  else if( traceAttr.getTraceSet() == false && traceAttr.getTraceGet() == true )
		  prepareTraceDirectiveInject(traceDirective,t,attr,attrCode,conditionType,"getMethod");
	  else if( traceAttr.getTraceSet() == true && traceAttr.getTraceGet() == true )
	  {
		  prepareTraceDirectiveInject(traceDirective,t,attr,attrCode,conditionType,"setMethod");
		  prepareTraceDirectiveInject(traceDirective,t,attr,attrCode,conditionType,"getMethod");
	  }
  }
  
  // Assigns and prepares trace code injection before calling "injectTraceDirective"
  //  + setMethod: What is the name of the setMethod we are attaching the trace to
  //  + attrCode: What is the trace code that should be executed
  public static void prepareTraceDirectiveInject( TraceDirective traceDirective, CodeTranslator t, Attribute attr, String attrCode, String conditionType, String method) 
  {
	  Map<String,String> lookups = new HashMap<String,String>();
	  lookups.put("Code",attrCode);
	  if( method.equals("setMethod") )
		  lookups.put("setMethod",t.translate("setMethod",attr));
	  if( method.equals("getMethod") )
		  lookups.put("getMethod",t.translate("getMethod",attr));
	  String injectionType = "after";
	    
	  if( "where".equals(conditionType) )
		  injectionType = "before";  
	  else if( "until".equals(conditionType) || "after".equals(conditionType) )
		  injectionType = "after";
	  if( method.equals("setMethod") )
		  injectTraceDirective(traceDirective,lookups,injectionType,"setMethod");
	  if( method.equals("getMethod") )
		  injectTraceDirective(traceDirective,lookups,injectionType,"getMethod");
  }
  
  public static void prepareTraceDirectiveInjectStateMachine( TraceDirective traceDirective, CodeTranslator t, StateMachine stm, String stmCode, String injectionType) 
  {
	  Map<String,String> lookups = new HashMap<String,String>();
	  lookups.put("Code",stmCode);
	  lookups.put("setMethod",t.translate("setMethod",stm));
	  injectTraceDirective(traceDirective,lookups,injectionType,"setMethod");
  }
  
  public static void tmp( TraceDirective traceDirective, CodeTranslator t, StateMachine stm, String stmCode, String injectionType) 
  {
	  Map<String,String> lookups = new HashMap<String,String>();
	  lookups.put("Code",stmCode);
	  lookups.put("exitMethod",t.translate("exitMethod",stm));
	  tmp2(traceDirective,lookups,injectionType);
  }
  public static void tmp2(TraceDirective traceDirective, Map<String,String> lookups, String injectionType)
  {
    UmpleClass aClass = traceDirective.getUmpleClass();
    String setMethod = lookups.get("exitMethod");
    String code = lookups.get("Code");

    CodeInjection set = new CodeInjection(injectionType, setMethod, code);
    set.setIsInternal(true);
    aClass.addCodeInjection(set);  
  }
  // Inject the necessary "before" and "after" hooks to call the trace, this method expects the following action semantic lookups
  //  + setMethod: What is the name of the setMethod we are attaching the trace to
  //  + attributeCode: What is the trace code that should be executed
  public static void injectTraceDirective(TraceDirective traceDirective, Map<String,String> lookups, String injectionType, String method)
  {
    UmpleClass aClass = traceDirective.getUmpleClass();
    String Method = lookups.get(method);
    String code = lookups.get("Code");

    CodeInjection set = new CodeInjection(injectionType, Method, code);
    set.setIsInternal(true);
    aClass.addCodeInjection(set);  
  }

  // Add a StringTracer class to support "String" tracing - typically used for testing, this methods 
  // expects the following action semantic lookups
  //  + packageName: What package should this class belong to?
  //  + extraCode: What is the code required to execute the trace 
  public static void prepareStringTracer(UmpleModel model, Map<String,String> lookups)
  {
    UmpleClass aClass = model.addUmpleClass("StringTracer");
    
    if (aClass.numberOfAttributes() == 0)
    {
      aClass.setIsInternal(true);
      aClass.setIsSingleton(true); 
      aClass.setPackageName(lookups.get("packageName"));
      Attribute traces = new Attribute("traces","String",null,null,false);
      traces.setIsList(true);
      aClass.addAttribute(traces);
      aClass.appendExtraCode(lookups.get("extraCode"));
    }
    aClass.createGeneratedClass(model);
  }
}