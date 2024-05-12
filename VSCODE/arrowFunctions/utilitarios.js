/*recuperarTexto=function(idComponente){
    let componente=document.getElementById(idComponente);
    let valorIngresado=componente.value;
    return valorIngresado;
}*/

//arrow FUNCTIONS
recuperarTexto=(idComponente)=>{
    let componente=document.getElementById(idComponente);
    let valorIngresado=componente.value;
    return valorIngresado;
}
recuperarInt=(idComponente)=>{
    let valorTexto=recuperarTexto(idComponente);
    let valorEntero=parseInt(valorTexto);
    return valorEntero;
}
recuperarFloat=(idComponente)=>{
    let valorTexto=recuperarTexto(idComponente);
    let valorFloat=parseFloat(valorTexto);
    return valorFloat;
}