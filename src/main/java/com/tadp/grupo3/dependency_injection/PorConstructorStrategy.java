package com.tadp.grupo3.dependency_injection;
import java.awt.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class PorConstructorStrategy implements ConstruccionStrategy {

	public <T> T instanciar(Class<T> unaClase, Contexto unContexto) {
	
		Constructor<?> unConstructor = this.obtenerConstructorPara(unaClase, unContexto);
				
		ArrayList<Object> objetosParaElConstructor = new ArrayList<Object>();
		
		for(Class<?>unTipoDeParametro: unConstructor.getParameterTypes()){			
			objetosParaElConstructor.add(this.instanciar(unTipoDeParametro, unContexto));
		}
		
		try{
			return (T) unConstructor.newInstance(objetosParaElConstructor.toArray());
		}catch(Exception e){
			
		}
		return null;
	}
	

	private Constructor obtenerConstructorPara(Class<?> unaClase, Contexto unContexto){
		
		/* TODO: si está bindeada, debería usar el binding. 
		   		 si no, debería usar la clase en si misma */
		
		Constructor[] unosConstructores = unaClase.getDeclaredConstructors();
		 
		 ArrayList<Constructor> unosConstructoresValidos = new ArrayList<Constructor>();
	
		 for(Constructor unConstructor: unosConstructores){
			 try{
				 unContexto.comprobarInstanciacionDelConstructor(unConstructor);
				 unosConstructoresValidos.add(unConstructor);
			 }catch(FaltaBindingException ex){
				 
			 }			 
		 }
		 
		 if(!(unosConstructoresValidos.size()==1))
			 throw new NoHayExactamenteUnSoloConstructorValidoException();
		 
		 return unosConstructoresValidos.get(0);
	}
}
