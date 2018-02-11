package prism.mediscan.details;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;

import prism.mediscan.R;
import prism.mediscan.model.Interaction;
import prism.mediscan.model.Specialite;

/**
 * Created by joshua-hugo on 11/02/18.
 */

public class InteractionExpandableAdapter extends ExpandableRecyclerAdapter<InteractionExpandableAdapter.InteractionListItem> {
    public static int TYPE_DETAIL = 1001;
    private Specialite specialite;
    private Context context;

    public static class InteractionListItem extends ExpandableRecyclerAdapter.ListItem{
        String nomMedicament;
        String description;
        String conseil;

        public InteractionListItem(String nomMedicament){
            super(TYPE_HEADER);
            this.nomMedicament = nomMedicament;
        }

        public InteractionListItem(String nomMedicament, String description, String conseil){
            super(TYPE_DETAIL);
            this.nomMedicament = nomMedicament;
            this.description = description;
            this.conseil = conseil;
        }


    }

    public class DetailViewHolder extends ViewHolder {

        TextView description;
        TextView conseil;

        public DetailViewHolder(View view){
            super(view);
            this.description = view.findViewById(R.id.interactionDescription);
            this.conseil = view.findViewById(R.id.interactionConseil);
        }

        public void bind(int position){
            this.description.setText(Html.fromHtml(visibleItems.get(position).description));
            this.conseil.setText(Html.fromHtml(visibleItems.get(position).conseil));
        }
    }

    public class InteractionViewHolder extends HeaderViewHolder {

        TextView nomMedicament;

        public InteractionViewHolder(View view){
            super(view, (ImageView) view.findViewById(R.id.item_arrow));
            this.nomMedicament = view.findViewById(R.id.nomMedicament);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            this.nomMedicament.setText(visibleItems.get(position).nomMedicament);
        }
    }

    public InteractionExpandableAdapter(Context context, Specialite specialite){
        super(context);
        this.context = context;
        this.specialite = specialite;
        setItems(getInteraction());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new InteractionViewHolder(inflate(R.layout.interaction_layout, parent));
            default:
                return new DetailViewHolder(inflate(R.layout.layout_interaction_detail, parent));
        }


    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                ((InteractionViewHolder) holder).bind(position);
                return;
            default:
                ((DetailViewHolder) holder).bind(position);
                return;
        }
    }

    public ArrayList<InteractionListItem> getInteraction(){
        final ArrayList<Interaction> interactions = this.specialite.getInteractions();
        ArrayList<InteractionListItem> list = new ArrayList<InteractionListItem>(interactions.size());

        for(Interaction interaction : interactions){
            list.add(new InteractionListItem(interaction.getNomMedicament2()));
            list.add(new InteractionListItem(interaction.getNomMedicament2(), interaction.getDescription(), interaction.getConseil()));
        }

        return list;
    }
}
