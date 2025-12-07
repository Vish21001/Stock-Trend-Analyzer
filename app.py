import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
import requests

# --- Fetch stock data from API ---
API_KEY = "YOUR_API_KEY"
symbol = "AAPL"
url = f"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol={symbol}&apikey={API_KEY}"
response = requests.get(url).json()

# Parse data
data = response["Time Series (Daily)"]
df = pd.DataFrame(data).T
df = df[['4. close']].astype(float)
df = df.rename(columns={'4. close':'Close'})
df = df.sort_index()  # oldest to newest

# Prepare data for prediction
df['Day'] = np.arange(len(df))
X = df[['Day']].values
y = df['Close'].values

# Train simple Linear Regression model
model = LinearRegression()
model.fit(X, y)

# Predict next 10 days
future_days = np.arange(len(df), len(df)+10).reshape(-1,1)
predictions = model.predict(future_days)

# Visualize
plt.figure(figsize=(12,6))
plt.plot(df['Day'], df['Close'], label="Historical Close")
plt.plot(future_days, predictions, label="Predicted Close", linestyle="--")
plt.title(f"{symbol} Stock Price Prediction")
plt.xlabel("Days")
plt.ylabel("Price ($)")
plt.legend()
plt.show()
