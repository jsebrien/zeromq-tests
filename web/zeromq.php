<?php 

class TransportLayerMessage {
	
	private $subject=null;
	private $object=null;
	
	public function setSubject($subjectParam=null) {
		if($subjectParam===null) return false;
		$this->subject = $subjectParam;
	}
	
	
	public function setObject($objectParam=null) {
		if($objectParam===null) return false;
		$this->object = $objectParam;
	}
	
	public function set($data) {
		foreach ($data AS $key => $value) $this->{$key} = $value;
	}
	
	public function getJSONEncode() {
		return json_encode(get_object_vars($this));
	}
}

$response = "";
$class = "";
$duration = "";
if (isset($_POST['valider']))
{
	$subject = $_POST['subject'];
	$object = $_POST['object'];
	
	$ctx = new ZMQContext();
	$req = new ZMQSocket($ctx, ZMQ::SOCKET_REQ);
	$req->connect("tcp://localhost:5555");
	
	$o = new TransportLayerMessage;
	$o->setSubject($subject);
	$o->setObject($object);
	$toSend = $o->getJSONEncode();
	$req->send($toSend);
	$response = $req->recv();
	$response = json_decode($response);
	$class = new TransportLayerMessage();
	$class->set($response);
}
?> 

<html>
<header>
<title>Test ZeroMQ</title>
</header>
<body>
	<table width="1300" align="center" border="1">
		
		<?php 
			if(!empty($class)){
				echo '<tr><td align="center" colspan="2"><font color="blue"><b>Response=',print_r($class),'</b></font></td></tr>';
			}
		?>  
		<form name="submission" method="post" action="testzmq.php" enctype="multipart/form-data">
		
		<tr>
			<td >subject:</td>
			<td ><input size="184" type="text" name="subject" /></td>
		</tr>
		<tr>
			<td >object:</td>
			<td ><input size="184" type="text" name="object" /></td>
		</tr>
		
		<tr><td  colspan="2" align="center"><input type="submit" name="valider" value="ASK"/></td></tr>
		</form>
	</table>
</body>
</html>
