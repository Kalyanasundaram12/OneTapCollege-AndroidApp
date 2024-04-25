package com.kalyan.onetapcollege;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private Context context;
    private List<PdfItem> pdfItemList;

    public PdfAdapter(Context context, List<PdfItem> pdfItemList) {
        this.context = context;
        this.pdfItemList = pdfItemList;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_card, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        PdfItem pdfItem = pdfItemList.get(position);
        holder.textCoursePdfId.setText(pdfItem.getCoursePdfId());
        holder.textPdfName.setText(pdfItem.getPdfName());

        // Set OnClickListener for the download image
        holder.imageDownload.setOnClickListener(v -> downloadPdf(pdfItem.getPdfUrl(), pdfItem.getPdfName()));
    }

    @Override
    public int getItemCount() {
        return pdfItemList.size();
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView textCoursePdfId;
        TextView textPdfName;
        ImageView imageDownload;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            textCoursePdfId = itemView.findViewById(R.id.text_course_pdf_id);
            textPdfName = itemView.findViewById(R.id.text_pdf_name);
            imageDownload = itemView.findViewById(R.id.image_download);
        }
    }

    private void downloadPdf(String pdfUrl, String pdfName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
        request.setTitle(pdfName);
        request.setDescription("Downloading PDF");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pdfName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(context, "Downloading PDF...", Toast.LENGTH_SHORT).show();
        }
    }
}
