<?php
	class Message {
		public $sender;
		public $body;

		function Message($sender="", $body=""){
			$this->sender = $sender;
			$this->body = $body;
		}

		function getJsonData(){
    		$var = get_object_vars($this);
    		foreach ($var as &$value) {
        		if (is_object($value) && method_exists($value,'getJsonData')) {
            		$value = $value->getJsonData();
        		}
    		}
    		return $var;
		}
	}

	/**
 	* recast stdClass object to an object with type
 	*
 	* @param string $className
 	* @param stdClass $object
 	* @throws InvalidArgumentException
 	* @return mixed new, typed object
 	*/
	function recast($new, stdClass &$object)
	{
    	foreach($object as $property => &$value)
    	{
        	$new->$property = &$value;
        	unset($object->$property);
    	}
    	unset($value);
    	$object = (unset) $object;
    	return $new;
	}

// If a GET request is made
if (isset($_GET["payload"])) {
	$stdObj = json_decode($_GET["payload"]);
	$message = recast(new Message(), $stdObj);
	$message->sender = "Server";
	$message->body = md5($message->body);
}

// If a POST request is made
else if (isset($_POST["payload"])) {
	$stdObj = json_decode($_POST["payload"]);
	$message = recast(new Message(), $stdObj);
	$message->sender = "Server";
	$message->body = md5($message->body);
}

else {
		$message = new Message();
		$message->sender = "Sender";
		$message->body = "Hi. You did not say anything.";
}

header("Content-type: application/json");
echo json_encode($message->getJsonData());
?>
