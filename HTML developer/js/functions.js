function isEven(number){
	return num % 2 === 0;
}

function factorial(number){
    if (number === 0) {return 1;}

	var prod = 1;
	while(number>0){
		prod = prod * number;
		number -- ;
	}
	return prod;
}

function kebabToSnake(string){

   var newStr = string.replace(/-/g,"_")
   return newStr;

}


