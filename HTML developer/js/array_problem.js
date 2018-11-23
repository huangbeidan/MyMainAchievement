
function printreverse(array){
	var temp = [];
	array.forEach(function(pos){
		temp.unshift(pos)
	})
	console.log(temp)
}

function isUniform(array){
	var isuniform = true;
	for (var i=0; i<array.length;i++){
		for(var j=1;j<array.length;j++){
			if (array[i]!=array[j]){
			isuniform = false;
			return isuniform;
			}
		}
	}
	return isuniform;
}

function sumArray(array){
	var sum = 0;
	array.forEach(function(pos){
		sum += pos;
	})
	console.log(sum);
}

function max(array){
	var max = array[0];
	array.forEach(function(num){
		if (max < num){
			max = num;
		}
	})
 return max;
}