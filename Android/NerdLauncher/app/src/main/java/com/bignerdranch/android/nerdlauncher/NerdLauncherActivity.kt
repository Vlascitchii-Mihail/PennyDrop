package com.bignerdranch.android.nerdlauncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.content.pm.ResolveInfo
import android.util.Log
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView

private const val TAG = "NerdLauncherActivity"

class NerdLauncherActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerd_launcher)

        //creating Recycler View's variable
        recyclerView = findViewById(R.id.app_recycler_view)

        //Recycler View's grid
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupAdapter()
    }

    private fun setupAdapter() {

        //creating implicit intent
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {

            //calls launchable apps
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        //returns ResolveInfo list with apps, which have CATEGORY_LAUNCHER filter
        val activities = packageManager.queryIntentActivities(startupIntent, 0)

        // sortWith() - sorting ResolveInfo metadata
        activities.sortWith(Comparator {a, b ->

            //CASE_INSENSITIVE_ORDER - A Comparator that orders strings ignoring character case.
            //loadLabel() - returns ResolveInfo objects Получить текущую текстовую метку, связанную
            // с этим элементом. Это вызовет обратный вызов данного PackageManager для загрузки метки из приложения.
            String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(packageManager).toString(),
                b.loadLabel(packageManager).toString())
        })

        Log.i(TAG, "Found ${activities.size}")

        //fill in the recyclerView.adapter
        recyclerView.adapter = ActivityAdapter(activities)
    }

    //View.OnClickListener - interface's implementation
    private inner class ActivityHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
//        private val nameTextView = itemView as TextView
        private val nameTextView = itemView.findViewById(R.id.label_text_id) as TextView
        private val appIcon = itemView.findViewById(R.id.label_list_id) as ImageView

        private lateinit var resolveInfo: ResolveInfo

        init {

            //click listener
            nameTextView.setOnClickListener(this)
        }

        //click listener
        override fun onClick(view: View) {

            //getting package and class names' information
            val activityInfo = resolveInfo.activityInfo

            //explicit intent
            //starting app from Recycler View list
            val intent = Intent(Intent.ACTION_MAIN).apply {

                //getting app package and class
                setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)

                //FLAG_ACTIVITY_NEW_TASK - starting a new activity in a new task
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val context = view.context

            //starting a new app
            context.startActivity(intent)
        }

        fun bindActivity(resolveInfo: ResolveInfo) {
            this.resolveInfo = resolveInfo
            val packageManager = itemView.context.packageManager
            val appName = resolveInfo.loadLabel(packageManager).toString()
            nameTextView.text = appName

            //icon for a new app
            appIcon.setImageDrawable(resolveInfo.loadIcon(packageManager))
        }
    }

    private inner class ActivityAdapter(val activities: List<ResolveInfo>): RecyclerView.Adapter<ActivityHolder>() {

        override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ActivityHolder {
//            val layoutInflater = LayoutInflater.from(container.context)
            val view = layoutInflater.inflate(R.layout.activity_icon_list, container, false)
            return ActivityHolder(view)
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val resolveInfo = activities[position]
            holder.bindActivity(resolveInfo)
        }

        override fun getItemCount(): Int {
            return activities.size
        }
    }
}