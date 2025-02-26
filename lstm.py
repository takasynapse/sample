import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, LabelEncoder
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense, Dropout
from tensorflow.keras.utils import to_categorical

# サンプルデータ生成 (加速度データの模擬データ)
def generate_dummy_data(samples=1000, time_steps=50, features=3, num_classes=2):
    X = np.random.randn(samples, time_steps, features)  # ランダムな時系列データ
    y = np.random.randint(0, num_classes, samples)      # ランダムなラベル
    return X, y

# データ準備
time_steps = 50   # 各サンプルの時系列の長さ
features = 3      # センサー軸数（例: ax, ay, az）
num_classes = 2   # クラス数（2クラス分類）

X, y = generate_dummy_data(samples=1000, time_steps=time_steps, features=features, num_classes=num_classes)

# ラベルをOne-Hotエンコード
y = to_categorical(y, num_classes=num_classes)

# データを訓練セットとテストセットに分割
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# LSTMモデルの構築
model = Sequential([
    LSTM(64, input_shape=(time_steps, features), return_sequences=True),
    Dropout(0.2),
    LSTM(32, return_sequences=False),
    Dropout(0.2),
    Dense(32, activation='relu'),
    Dense(num_classes, activation='softmax')  # 分類用のSoftmax層
])

# モデルのコンパイル
model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

# モデルの要約
model.summary()

# モデルの学習
history = model.fit(X_train, y_train, epochs=20, batch_size=32, validation_split=0.2)

# テストデータで評価
test_loss, test_accuracy = model.evaluate(X_test, y_test, verbose=0)
print(f"Test Loss: {test_loss:.4f}, Test Accuracy: {test_accuracy:.4f}")

# サンプルデータで予測
sample_data = X_test[:1]  # テストデータから1つのサンプルを取得
prediction = model.predict(sample_data)
print(f"Predicted probabilities: {prediction}")
print(f"Predicted class: {np.argmax(prediction)}")
