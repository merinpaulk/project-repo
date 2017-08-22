package enums;

public class PaymentStatusEnum extends P3SAbstractEnum {

	public static final String INITIATED = "Initiated";
	public static final String PENDING = "Pending"; // Wouldn't 'Awaiting Funds'
													// be a better title? acTidy
	public static final String FUNDS_RECEIVED = "Funds Received";
	public static final String FUNDS_SENT = "Funds Sent";
	public static final String EPO_RECEIVED = "EPO Received";
	public static final String EPO_INSTRUCTED = "EPO Instructed";
	public static final String FAILED = "Failed";
	public static final String COMPLETED = "Completed";

	// Yes - is an oddity here. If client pays a penalty, we may not know which
	// status they used!

	// Constructor - Which verifies the value provided
	public PaymentStatusEnum(String status) {
		if (status == null)
			fail("PaymentStatusEnum constructor passed null");

		String sofar = null;

		if (status.equalsIgnoreCase(PaymentStatusEnum.INITIATED) || status.equalsIgnoreCase("INITIATED"))
			sofar = PaymentStatusEnum.INITIATED;

		if (status.equalsIgnoreCase(PaymentStatusEnum.PENDING) || status.equalsIgnoreCase("PENDING"))
			sofar = PaymentStatusEnum.PENDING;

		if (status.equalsIgnoreCase(PaymentStatusEnum.FUNDS_RECEIVED) || status.equalsIgnoreCase("FUNDS_RECEIVED"))
			sofar = PaymentStatusEnum.FUNDS_RECEIVED;

		if (status.equalsIgnoreCase(PaymentStatusEnum.FUNDS_SENT) || status.equalsIgnoreCase("FUNDS_SENT"))
			sofar = PaymentStatusEnum.FUNDS_SENT;

		if (status.equalsIgnoreCase(PaymentStatusEnum.EPO_RECEIVED) || status.equalsIgnoreCase("EPO_RECEIVED"))
			sofar = PaymentStatusEnum.EPO_RECEIVED;

		if (status.equalsIgnoreCase(PaymentStatusEnum.EPO_INSTRUCTED) || status.equalsIgnoreCase("EPO_INSTRUCTED"))
			sofar = PaymentStatusEnum.EPO_INSTRUCTED;

		if (status.equalsIgnoreCase(PaymentStatusEnum.FAILED) || status.equalsIgnoreCase("FAILED"))
			sofar = PaymentStatusEnum.FAILED;

		if (status.equalsIgnoreCase(PaymentStatusEnum.COMPLETED) || status.equalsIgnoreCase("COMPLETED"))
			sofar = PaymentStatusEnum.COMPLETED;

		if (sofar != null) {
			this.value = sofar.toString();
		} else {
			fail("PaymentStatusEnum constructor passed invalid status: " + status);
		}
	}

}
