This is the source code of BSPCOVER.

We implemented the proposed algorithms in JAVA.

Functions:

0. Both timeseries dataset and shapelet dataset are well-prepared in the program directory. You are only required follow the following procedures to learn the basic functions.

1. Load timeseries:
* Click button "Load Dataset".
* Select the directory "Grace_MI" and click the "Choose" button on the right bottom. (You are not required to enter the directory. If you do enter the "Grace_MI" directory and see the files "Grace_MI_TEST" and "Grace_MI_TRAIN", please step backward to the directory state. You are only required to select the directory "Grace_MI".)

2. Load shapelets:
* Click button "Load Shapelet"
* Select the directory "shapelet&weight" and click the "Choose" button on the right bottom. (You are not required to enter the directory. If you do enter the "shapelet&weight" directory and see the files "shapelet-original.txt" and "shapelet-weight.txt", please step backward to the directory state. You are only required to select the directory "shapelet&weight".)

3. Use the default setting "Off" state in "Stack model" above two center charts .

4. You are allowed to switch between "Dot" and "Line" models to see which model explains the data better in "Line/Dot Trace" next to the "Stack model".

5. You are not required to know the functions in the "BSPCOVER" panel and no need to input any parameters in that section.

6. The "10 minimum distances (10 time series to one shapelet)" section next to the two center charts contains 10 timeseries data. These 10 timeseris are top 10 ranked instances with smallest distances related to the current shapelet selected.

7. The blue-red histogram on the upper right shows the distance distribution of all-timeseries-to-current-shapelet.
* Red color represents timeseries's distances from class 0 (label 0).
* Blue color represents timeseries's distances from class 1 (label 1).

8. The bar chart on the right bottom shows top 10 ranked shapelet data.
* Name format: S[a][b]. "S" represents Shapelet. "a" is the class number, "b" is the shapelet number, e.g., S[0][1] stands for the selection 0 in the list "Label" and 1 in the list "Shapelets" under the "Load Shapelet" button on the left bottom.
* The smaller the weight of the shapelet, the more timeseries are related to it.

