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
import prism.mediscan.model.Avis;
import prism.mediscan.model.Specialite;

/**
 * Created by rapha on 05/02/2018.
 */

public class AvisExpandableAdapter extends ExpandableRecyclerAdapter<AvisExpandableAdapter.AvisListItem> {
    public static int TYPE_DETAIL = 1001;
    private Specialite specialite;
    private Context context;

    public static class AvisListItem extends ExpandableRecyclerAdapter.ListItem {
        String dateAvis;
        String titreAvis;
        String contenu;

        public AvisListItem(String titreAvis, String dateAvis) {
            super(TYPE_HEADER);
            this.dateAvis = dateAvis;
            this.titreAvis = titreAvis;
        }

        public AvisListItem(String titreAvis, String dateAvis, String contenu) {
            super(TYPE_DETAIL);
            this.titreAvis = titreAvis;
            this.dateAvis = dateAvis;
            this.contenu = contenu;

        }
    }

    public class DetailViewHolder extends ViewHolder {

        TextView contenu;

        public DetailViewHolder(View view) {
            super(view);
            // store view items
            this.contenu = view.findViewById(R.id.avisContenu);
            // click listener

        }

        public void bind(int position) {
            // bind data to view
            this.contenu.setText(Html.fromHtml(visibleItems.get(position).contenu));
            // animation
        }
    }

    public class AvisViewHolder extends HeaderViewHolder {

        TextView titreAvis;
        TextView dateAvis;

        public AvisViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));
            this.dateAvis = view.findViewById(R.id.dateAvis);
            this.titreAvis = view.findViewById(R.id.titreAvis);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            this.titreAvis.setText(visibleItems.get(position).titreAvis);
            this.dateAvis.setText(visibleItems.get(position).dateAvis);
        }
    }

    public AvisExpandableAdapter(Context ctx, Specialite specialite) {
        super(ctx);
        this.specialite = specialite;
        this.context = ctx;
        setItems(getAvis());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new AvisViewHolder(inflate(R.layout.avis_layout, parent));
            default:
                return new DetailViewHolder(inflate(R.layout.layout_avis_detail, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((AvisViewHolder) holder).bind(position);
                return;
            default:
                ((DetailViewHolder) holder).bind(position);
                return;
        }
    }

    public ArrayList<AvisListItem> getAvis() {
        final ArrayList<Avis> avis = this.specialite.getAvis();
        ArrayList<AvisListItem> list = new ArrayList<AvisListItem>(avis.size());

        for (Avis avi : avis) {
            list.add(new AvisListItem(avi.getTitre(), avi.getDate()));
            list.add(new AvisListItem(avi.getTitre(), avi.getDate(), avi.getContenu()));
        }

        return list;
    }
}
