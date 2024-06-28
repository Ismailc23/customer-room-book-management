function dateValidation() {
    const startDateInput = document.getElementById('stayStartDate').value;
    const endDateInput = document.getElementById('stayEndDate').value;

    const startDate = new Date(startDateInput);
    const endDate = new Date(endDateInput);
    const currentDate = new Date();


    if (!startDateInput || !endDateInput) {
        event.preventDefault();
        alert("Both start date and end date are required.");
    }

    if (endDate < startDate) {
        event.preventDefault();
        alert("End date should not be before Start Date");
    }

    const daysBetween = (endDate - startDate) / (1000 * 60 * 60 * 24);
    if (daysBetween > 30) {
        event.preventDefault();
        alert("Maximum stay duration is 30 days.");
    }
}

