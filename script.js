fetch("data.json")
    .then(response => response.json())
    .then(data => {
        const ctx = document.getElementById('stockChart').getContext('2d');
        const labels = data.historical.map((_,i)=>i).concat(
            Array.from({length: data.predicted.length}, (_,i)=>data.historical.length+i)
        );
        const values = data.historical.concat(data.predicted);
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: `${data.symbol} Stock Price`,
                    data: values,
                    borderColor: 'blue',
                    fill: false
                }]
            },
        });
    });
