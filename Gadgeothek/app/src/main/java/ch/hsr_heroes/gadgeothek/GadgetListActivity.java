package ch.hsr_heroes.gadgeothek;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hsr_heroes.gadgeothek.domain.Gadget;
import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public class GadgetListActivity extends BaseActivity {

    private RecyclerView myGadgetsList;
    private View gadgetsEmptyPlaceholder;
    private TextView gadgetsEmptyText;
    private GadgetAdapter gadgetAdapter;

    protected void onCreateMainContent(ViewGroup contentView) {
        setTitle("All Gadgets");
        LayoutInflater.from(this).inflate(R.layout.activity_all_gadgets, contentView, true);
        
        gadgetsEmptyPlaceholder = findViewById(R.id.no_gadgets_empty_placeholder);
        gadgetsEmptyText = (TextView) findViewById(R.id.no_gadgets_empty_text);
        myGadgetsList = (RecyclerView) findViewById(R.id.my_gadgets_list);

        myGadgetsList.setLayoutManager(new LinearLayoutManager(this));
        gadgetAdapter = new GadgetAdapter();
        myGadgetsList.setAdapter(gadgetAdapter);
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                gadgetAdapter.setGadgetList(input);
                if(input.isEmpty()){
                    gadgetsEmptyText.setText("No Gadgets at this School... Go Go Inspector Gadget =)");
                    gadgetsEmptyPlaceholder.setVisibility(View.VISIBLE);
                }else{
                    gadgetsEmptyPlaceholder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(GadgetListActivity.this, "Error Loading Gadgets "+message, Toast.LENGTH_LONG).show();
                gadgetsEmptyText.setText(message);
                gadgetsEmptyPlaceholder.setVisibility(View.VISIBLE);
            }
        });
    }
    private class GadgetAdapter extends RecyclerView.Adapter<GadgetAdapter.GadgetViewHolder>{

        private final List<Gadget> gadgets = new ArrayList<>();
        @Override
        public GadgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GadgetViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_gadget, parent, false));
        }

        @Override
        public void onBindViewHolder(GadgetViewHolder holder, int position) {
            Gadget g = gadgets.get(position);
            holder.name.setText(g.getName());
        }

        @Override
        public int getItemCount() {
            return gadgets.size();
        }

        public void setGadgetList(List<Gadget> input) {
            gadgets.clear();
            gadgets.addAll(input);
            notifyDataSetChanged();
        }

        class GadgetViewHolder extends RecyclerView.ViewHolder{

            private final TextView name;

            public GadgetViewHolder(View itemView) {
                super(itemView);
                this.name = (TextView) itemView.findViewById(R.id.my_gadget_name);
            }
        }

    }
}
