/*ejecutarSumar=function(){
    let valor1=recuperarTexto("txtValor1");
    let valor2=recuperarTexto("txtValor2");
    console.log(">>>VALOR 1: "+valor1+ " >>>VALOR 2: "+valor2);
}*/

//ARROW FUNCTIONS
ejecutarOperacion=(operar)=>{
    let valor1=recuperarInt("txtValor1");
    let valor2=recuperarInt("txtValor2");
    let resultado;
    resultado=operar(valor1,valor2);
    console.log(resultado);
}

sumar=(sum1,sum2)=>{
    let resultado= sum1+sum2;
    return resultado;
}
restar=(rest1,rest2)=>{
    let resultado=rest1-rest2;
    return resultado;
}

ejecutar=(fn)=>{
    console.log("estoy ejecutando funciones....");
    fn();
}

imprimir=()=>{
    console.log("Estoy imprimiendo");
}
saludar=()=>{
    alert("aprendiendo paps");
}
testEjecutar=()=>{
    ejecutar(imprimir);
    ejecutar(saludar);
    ejecutar(()=>{
        alert("anonimo paps");
    });
}
