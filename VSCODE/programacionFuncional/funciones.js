/*ejecutarSumar=function(){
    let valor1=recuperarTexto("txtValor1");
    let valor2=recuperarTexto("txtValor2");
    console.log(">>>VALOR 1: "+valor1+ " >>>VALOR 2: "+valor2);
}*/

//ARROW FUNCTIONS
ejecutarSumar=()=>{
    let valor1=recuperarInt("txtValor1");
    let valor2=recuperarInt("txtValor2");
    let resultadoSuma;
    console.log(">>>VALOR 1: "+valor1+ " >>>VALOR 2: "+valor2);
    resultadoSuma=sumar(valor1,valor2);
    console.log(resultadoSuma);
    
}
ejecutarRestar=()=>{
    let valor1=recuperarFloat("txtValor1");
    let valor2=recuperarFloat("txtValor2");
    let resultadoResta=restar(valor1,valor2);
    console.log("Resultado de la resta: "+resultadoResta);
}

sumar=(sum1,sum2)=>{
    let resultado= sum1+sum2;
    return resultado;
}
restar=(rest1,rest2)=>{
    let resultado=rest1-rest2;
    return resultado;
}