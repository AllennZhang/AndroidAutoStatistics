package com.hipac.codeless.agent;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hipac.codeless.R;
import com.hipac.codeless.config.Constants;
import com.hipac.codeless.core.TraceService;
import com.hipac.codeless.util.MsgLogger;
import com.hipac.codeless.util.StringUtil;
import com.hipac.codeless.util.TraceHolder;


/**
 *
 *
 * todo 在该类中添加要注入的具体代码
 */

public class PluginAgent {

    private static final int TRACE_DATA_ID = 0x7b00a1;
    private final static int LABEL_ID = 0x7b00b1;
    private static final String TAG = PluginAgent.class.getSimpleName();

    private static String[] ignorePages = {"com.yt.xxx"};


    /**
     * Proxy android.view.onClick(View view)
     */
    public static void onClick(View view) {
        String viewPath = getViewPath(view, new StringBuilder());
        String page = getViewPage(view);
        String extendFields = (String) view.getTag(TRACE_DATA_ID);
        String label = (String) view.getTag(LABEL_ID);
        MsgLogger.e("extendfields:"+extendFields+"-----label:"+label);
        resetUtrpUrl(page);
        TraceService.onEvent(page, "",label,TraceHolder.getUtrpUrl(),"",viewPath,extendFields == null ? "" : extendFields);
        TraceHolder.changeRefPage(viewPath);

    }


    /**
     * Proxy android.content.DialogInterface.onClick(DialogInterface dialog, int which)
     */
    public static void onClick(Object object, DialogInterface dialogInterface, int which) {

    }


    /**
     * Proxy android.widget.AdapterView.OnItemClickListener
     * onItemClick(AdapterView<?> parent, View view, int position, long id)
     */
    public static void onItemClick(Object object, AdapterView parent, View view, int position, long id) {
        String viewPath = getViewPath(view, new StringBuilder());
        String page = getViewPage(view);
        String extendFields = (String) view.getTag(TRACE_DATA_ID);
        String label = (String) view.getTag(LABEL_ID);
        resetUtrpUrl(page);
        TraceService.onEvent(page, "",label,TraceHolder.getUtrpUrl(),"",viewPath,extendFields == null ? "" : extendFields);
        TraceHolder.changeRefPage(viewPath);
    }


    /**
     * Proxy android.widget.AdapterView.OnItemSelectedListener
     * onItemSelected(AdapterView<?> parent, View view, int position, long id)
     */
    public static void onItemSelected(Object object, AdapterView parent, View view, int position, long id) {
        onItemClick(object, parent, view, position, id);
    }


    /**
     * Proxy  android.widget.ExpandableListView.OnGroupClickListener
     * onGroupClick(ExpandableListView parent, View v, int groupPosition,long id)
     */
    public static void onGroupClick(Object object, ExpandableListView parent, View v, int groupPosition, long id) {

    }


    /**
     * Proxy android.widget.ExpandableListView.OnGroupClickListener.OnChildClickListener
     * onChildClick(ExpandableListView parent, View v, int groupPosition,int childPosition, long id)
     */
    public static void onChildClick(Object object, ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

    }


    /**
     * Proxy android.widget.SeekBar.OnSeekBarChangeListener
     * <p>
     * onStopTrackingTouch(SeekBar seekBar)
     *
     * @param thisObj
     * @param seekBar
     */
    public static void onStopTrackingTouch(Object thisObj, SeekBar seekBar) {

    }

    /**
     * Proxy android.widget.RatingBar.OnRatingBarChangeListener
     * onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
     */
    public static void onRatingChanged(Object thisObj, RatingBar ratingBar, float rating, boolean fromUser) {

    }


    /**
     * Proxy android.widget.RadioGroup.OnCheckedChangeListener
     * onCheckedChanged(RadioGroup group, @IdRes int checkedId)
     */
    public static void onCheckedChanged(Object object, RadioGroup radioGroup, int checkedId) {
        if (radioGroup == null) return;
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            if (child != null && child.getId() == checkedId) {
                String viewPath = getViewPath(child, new StringBuilder());
                String viewPage = getViewPage(child);
                String extendFields = (String) child.getTag(TRACE_DATA_ID);
                String label = (String) child.getTag(LABEL_ID);
                resetUtrpUrl(viewPage);
                TraceService.onEvent(viewPage, "",label,TraceHolder.getUtrpUrl(),"",viewPath,extendFields == null ? "" : extendFields);
                TraceHolder.changeRefPage(viewPath);
                break;
            }
        }
    }


    /**
     * Proxy android.widget.CompoundButton.OnCheckedChangeListener
     * onCheckedChanged(CompoundButton buttonView, boolean isChecked)
     */
    public static void onCheckedChanged(Object object, CompoundButton button, boolean isChecked) {
        if (isChecked) {
            View view = button;
            String viewPath = getViewPath(view, new StringBuilder());
            String page = getViewPage(view);
            String extendFields = (String) view.getTag(TRACE_DATA_ID);
            String label = (String) view.getTag(LABEL_ID);
            resetUtrpUrl(page);
            TraceService.onEvent(page, "",label,TraceHolder.getUtrpUrl(),"",viewPath,extendFields == null ? "" : extendFields);
            TraceHolder.changeRefPage(viewPath);
        }

    }


    /**
     * Proxy android.app.Fragment android.support.v4.app.Fragment
     * onActivityCreated(Bundle savedInstanceState)
     *
     * @param obj
     */
    public static void onFragmentActivityCreated(Object obj) {
        if (obj == null) return;
        String hostName = "";
        String pageName = obj.getClass().getSimpleName();
        if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            if (fragment.getActivity() != null) {
                hostName = fragment.getActivity().getClass().getSimpleName();
            }
            TraceService.setCurrentPage(hostName + "/" + pageName);
        } else if (obj instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) obj;
            if (fragment.getActivity() != null) {
                hostName = fragment.getActivity().getClass().getSimpleName();
            }
            TraceService.setCurrentPage(hostName + "/" + pageName);
        }
    }

    /**
     * Proxy android.app.Fragment android.support.v4.app.Fragment
     * onResume()
     *
     * @param obj
     */
    public static void onFragmentResume(Object obj) {
        if (obj == null) return;
        String hostName = "";
        String pageName = obj.getClass().getSimpleName();
        if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            if (fragment.getActivity() != null) {
                hostName = fragment.getActivity().getClass().getSimpleName();
            }
            resetUtrpUrl(pageName);
            TraceService.onPageStart(hostName + "/" + pageName, TraceHolder.getReferencePage(),"",TraceHolder.getUtrpUrl(),TraceHolder.pageExtendsMap.get(pageName) == null ? "" : TraceHolder.pageExtendsMap.get(pageName));
            TraceHolder.changeRefPage(hostName + "/" + pageName);
            TraceService.setCurrentPage(hostName + "/" + pageName);
        } else if (obj instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) obj;
            if (fragment.getActivity() != null) {
                hostName = fragment.getActivity().getClass().getSimpleName();
            }
            resetUtrpUrl(pageName);
            TraceService.onPageStart(hostName + "/" + pageName, TraceHolder.getReferencePage(),"",TraceHolder.getUtrpUrl(),TraceHolder.pageExtendsMap.get(pageName) == null ? "" : TraceHolder.pageExtendsMap.get(pageName));
            TraceHolder.changeRefPage(hostName + "/" + pageName);
            TraceService.setCurrentPage(hostName + "/" + pageName);
        }
    }

    /**
     * Proxy android.app.Fragment android.support.v4.app.Fragment
     * onPause()
     *
     * @param obj
     */
    public static void onFragmentPause(Object obj) {

    }


    /**
     * Proxy android.app.Fragment android.support.v4.app.Fragment
     * setUserVisibleHint(boolean isVisibleToUser)
     */
    public static void setFragmentUserVisibleHint(Object fragmentObject, boolean isUserVisibleHint) {

    }


    /**
     * *Proxy android.app.Fragment android.support.v4.app.Fragment
     * onHiddenChanged(boolean hidden)
     */
    public static void onFragmentHiddenChanged(Object fragment, boolean hidden) {
        setFragmentUserVisibleHint(fragment, !hidden);
    }


    /**
     * Proxy android.app.Activity
     * onCreate(Bundle saveInstanceBundle)
     *
     * @param obj
     */

    public static void onActivityCreate(Object obj) {
        if (obj == null) return;
        TraceService.setCurrentPage(obj.getClass().getSimpleName());
    }


    /**
     * Proxy android.app.Activity
     * onNewintent(Intent intent)
     *
     * @param obj
     */
    public static void onActivityNewIntent(Object obj) {
        if (obj == null) return;
        TraceService.setCurrentPage(obj.getClass().getSimpleName());
    }

    /**
     * Proxy android.app.Activity
     * onResume()
     *
     * @param obj
     */
    public static void onActivityResume(Object obj) {
        if (obj == null) return;
        String fullName = obj.getClass().getName();
        String pageName = obj.getClass().getSimpleName();
        resetUtrpUrl(pageName);
        if (ignorePages != null && ignorePages.length > 0) {
            for (String ignorePage : ignorePages) {
                if (ignorePage.equals(fullName)) {
                    return;
                }
            }
        }
        TraceService.setCurrentPage(pageName);
        TraceService.onResume(pageName, TraceHolder.getReferencePage(), Constants.BUSINESS_TYPE_NATIVE_PAGE,"",TraceHolder.getUtrpUrl(),"",TraceHolder.pageExtendsMap.get(pageName)== null ? "" : TraceHolder.pageExtendsMap.get(pageName));
        TraceHolder.changeRefPage(pageName);

    }


    /**
     * Proxy android.app.Activity
     * onPause()
     * @param obj
     */
    public static void onActivityPause(Object obj) {

    }





    /**
     * todo
     * note:ContentFrameLayout对应的acticity，frgment等xml布局的顶层view，
     *      PopupDecorView对应PopupWindow布局的顶层view
     *      DecorView对应的Dialog布局的顶层view
     * @param view
     * @param builder
     * @return
     */
    public static String getViewPath(View view,StringBuilder builder){
        if (view == null ){
            return builder.toString();
        }
        if (view instanceof TextView){
            TextView tv = (TextView) view;
            if (!TextUtils.isEmpty(tv.getText())) {
                builder.append("[");
                builder.append(tv.getText());
                builder.append("]");
                builder.append("/");
            }
        }
        builder.append(view.getClass().getSimpleName());
        View currentView = view;
        ViewParent parent = view.getParent();
        int counter = 0;
        do {
            if (parent == null) {
                break;
            }
            //todo 防止异常情况下死循环，阻塞主线程
            if (counter >= 20){
                break;
            }
            String parentType = parent.getClass().getSimpleName();
            if (!StringUtil.empty(parentType)){
                builder.append("/");
                int index = -1;
                if (parent instanceof ViewGroup) {
                    //recyclerview viewholder复用问题，需要特殊处理
                    if (parent instanceof RecyclerView){
                        builder.append("_var_"+parentType);
                        RecyclerView recyclerView = (RecyclerView) parent;
                        int position = recyclerView.getChildAdapterPosition(currentView);
                        builder.append(String.format(view.getContext().getString(R.string.format_index), position));
                    }else if (parent instanceof ListView){
                        builder.append("_var_"+parentType);
                        ListView listView = (ListView) parent;
                        int position = listView.getPositionForView(currentView);
                        builder.append(String.format(view.getContext().getString(R.string.format_index), position));
                    }else {
                        builder.append(parentType);
                        index = ((ViewGroup) parent).indexOfChild(currentView);
                        if (index != -1) {
                            builder.append(String.format(view.getContext().getString(R.string.format_index), index));
                        }
                    }
                }
            }

            if (parentType.contains("DecorView") || parentType.contains("PopupDecorView")|| parentType.contains("ViewRootImpl")){
                break;
            }
            currentView = (View) parent;
            parent = parent.getParent();
            counter++;
        }while (! builder.toString().contains("ContentFrameLayout"));
        builder.append("/");
        builder.append(getViewPage(view));
        String viewPath = builder.toString();
        String[] stringArray = builder.toString().split("/");
        builder.delete(0,viewPath.length());
        if (stringArray != null){
            for (int i = stringArray.length;i>0;i--){
                builder.append(stringArray[i-1]);
                if (i > 1) {
                    builder.append("/");
                }
            }
        }
        MsgLogger.e("counter:"+counter);
        return builder.toString();

    }

    public static String getViewPage(View view) {
        if (view == null) return "unknownPage";
        Context context = view.getContext();
        if (context == null) return "unknownPage";
        if (context instanceof Activity) {
            String activityName = context.getClass().getSimpleName();
            return activityName;
        }else if (context instanceof ContextWrapper){
            ContextWrapper contextWrapper = (ContextWrapper) context;
            Context baseContext = contextWrapper.getBaseContext();
            String wraperName = baseContext.getClass().getSimpleName();
           return wraperName;
        }else {
            return "unknownPage";
        }
    }


    private static void resetUtrpUrl(String page){
        if (!"H5Page".equals(TraceHolder.getReferencePage())){
            TraceHolder.changeH5Url("");
            TraceHolder.changeUtrpUrl("");
        }
    }

}
