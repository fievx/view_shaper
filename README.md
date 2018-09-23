# view_shaper
A library to help create shaped views and layouts in Android

If you can write a Path representing the shape that you need, you can use ViewShaper to clip any View following the path and draw
a shadow. 

This is a nice solution when the desired shape is more complex than a circle or a square and you cannot create a XML Shape to use 
as background of your View.

### Implementation: 
Create a Custom View extending ViewShaper. ViewShaper has a method `abstract fun getShaper(): Shaper?` which you must override
to return a Shaper. 

Shaper is an Inerface: 
```kotlin
interface Shaper {
    fun getPath(width: Int, height: Int): Path
}
```

So a simple implementation of a custom view overriding a ViewShaper could look like this: 

```kotlin
class WeirdViewShaper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewShaper(context, attrs, defStyleAttr) {

    override fun getShaper(): Shaper? {
        return object : Shaper{
            override fun getPath(width: Int, height: Int): Path {
                val centerX = width/2f
                val centerYTop = height/3f
                val centerYBottom = height - height/3f
                val right = width.toFloat()
                val bottom = height.toFloat()
                return Path().apply {
                    moveTo(0f, 0f)
                    lineTo(centerX, centerYTop)
                    lineTo(right, 0f)
                    lineTo(right, bottom)
                    lineTo(centerX, centerYBottom)
                    lineTo(0f, bottom)
                    close()
                }
            }
        }
    }
}
```

You now have a layout that will accept a single child (much like a ScrollView). This Single child can be a View or a ViewGroup.
And you can obviously have a ViewShaper inside a ViewShaper since a ViewShaper is just a ViewGroup. 

```XML
        <android.support.constraint.ConstraintLayout
            android:layout_height="match_parent"
            android:layout_width="match_parent"
            >

            <ImageView
                android:layout_width="0dp"
                android:layout_height="0dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                android:background="@color/colorAccent" />

            <com.example.denais.testapplication.viewshaper.WeirdViewShaper
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:padding="3dp">

                <TextView
                    android:id="@+id/bt_shadow"
                    android:text="Another shape just for the fun"
                    android:layout_height="50dp"
                    android:layout_width="wrap_content"
                    android:background="@color/colorPrimary"
                    android:gravity="center"
                    android:textColor="@color/black"
                    android:padding="10dp"/>
            </com.example.denais.testapplication.viewshaper.WeirdViewShaper>

        </android.support.constraint.ConstraintLayout>
   ```
    
![image](https://image.noelshack.com/fichiers/2018/31/3/1533075290-device-2018-07-31-230959.png)
   
